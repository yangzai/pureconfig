package pureconfig

import scala.collection.mutable.ListBuffer
import scala.language.experimental.macros
import scala.reflect.macros.whitebox

trait Derivation[A] {
  def value: A
}

object Derivation {

  // A derivation for which an implicit value of `A` could be found.
  // TODO make `Derivation` subclasses private
  case class Successful[A](value: A) extends Derivation[A]

  // A derivation for which an implicit `A` could be found. This is only used internally by the `materializeDerivation`
  // macro - when a derivation requested directly by a user is successfully materialized, it is guaranteed to be a
  // `Derivation.Successful`.
  // TODO make `Derivation` subclasses private
  case class Failed[A]() extends Derivation[A] {
    def value = throw new IllegalStateException("Failed derivation")
  }

  implicit def materializeDerivation[A]: Derivation[A] = macro DerivationMacros.materializeDerivation[A]
}

class DerivationMacros(val c: whitebox.Context) {
  import c.universe._

  private[this] val thisPackage = TermName("pureconfig")

  private[this] implicit class RichType(val t: Type) {
    def toTree: Tree =
      if (t.typeArgs.isEmpty) tq"${t.typeSymbol}"
      else tq"${t.typeConstructor.toTree}[..${t.typeArgs.map(_.toTree)}]"
  }

  private[this] implicit class RichTree(val t: Tree) {
    def typecheck: Tree = c.typecheck(t, c.TYPEmode).tpe.dealias.toTree
    def toType: Type = c.typecheck(t, c.TYPEmode).tpe
  }

  // The entrypoint for materializing `Derivation` instances.
  def materializeDerivation[A: WeakTypeTag]: Tree = {
    // c.universe.asInstanceOf[scala.tools.nsc.Global].analyzer.resetImplicits()

    // if `false`, then this is a `Derivation` triggered inside another `Derivation`
    val isRootDerivation = c.openImplicits.count(_.pre =:= typeOf[Derivation.type]) == 1
    //    println(isRootDerivation + " " + isRootDerivation2)
    //    println(c.openMacros)

    //    println("At " + weakTypeOf[A])
    //    println("  - is root: " + isRootDerivation)

    try {
      if (isRootDerivation) {
        val t = materializeRootDerivation[A]
        println(show(t))
        t
      } else materializeInnerDerivation[A]

    } catch {
      case ex: Throwable =>
        if (!isRootDerivation) {
          println("aborting inner macro: " + weakTypeOf[A])
          ex.printStackTrace()
        }
        throw ex
    }
  }

  // Materialize a `Derivation` that was requested in the context of another.
  //
  // In this case a `Derivation` instance is always materialized so that the implicit search does not stop here. We
  // materialize a `Derivation.failed` for missing implicits so that the `Derivation` at the root level can easily find
  // and handle them.
  private[this] def materializeInnerDerivation[A: WeakTypeTag]: Tree = {
    //    println(weakTypeOf[A])
    //    println(c.openImplicits.head.pt.typeArgs.head.dealias)

    c.inferImplicitValue(weakTypeOf[A]) match {
      case EmptyTree =>
        println("failed to derive " + weakTypeOf[A])
        q"$thisPackage.Derivation.Failed[${weakTypeOf[A]}]()"

      case value =>
        q"$thisPackage.Derivation.Successful[${weakTypeOf[A]}]($value)"
    }
  }

  // Materialize a `Derivation` at the root level, i.e. not caused by the materialization of another `Derivation`.
  //
  // As usual, it searches for an implicit for `A`. If it fails, the macro aborts as expected, causing an implicit not
  // found error with a basic message. If an implicit is found, it still needs to search the tree found in order to
  // check if some inner derivations materialized a `Derivation.failed`. If that's the case, it collects those failures
  // and prints a nice message.
  private[this] def materializeRootDerivation[A: WeakTypeTag]: Tree = {
    c.inferImplicitValue(weakTypeOf[A]) match {
      case EmptyTree =>
        println("failed to derive " + weakTypeOf[A] + " (root)")

        // failed to find an implicit at the root level of the derivation; set a generic implicit not found message
        // without further information
        setImplicitNotFound(Nil)

        // cause the implicit to fail materializing - the message is ignored
        c.abort(c.enclosingPosition, "")

      case value =>
        // collect the failed derivations in the built implicit tree
        val failed = collectFailedDerivations(value)

        if (failed.isEmpty) {
          q"$thisPackage.Derivation.Successful[${weakTypeOf[A]}]($value)"
        } else {
          // if there are failures, that means one of the inner implicits was not found - set a message with details
          // about the paths failed
          setImplicitNotFound(failed)

          // cause the implicit to fail materializing - the message is ignored
          c.abort(c.enclosingPosition, "")
        }
    }
  }

  private[this] def collectFailedDerivations(tree: Tree): List[List[Tree]] = {
    val failures = ListBuffer[List[Tree]]()

    val traverser = new Traverser {
      private[this] var currentPath = List[Tree]()

      override def traverse(tree: Tree) = tree match {
        case q"${ Ident(`thisPackage`) }.Derivation.Successful.apply[$typ]($valueExpr)" =>
          currentPath = typ.typecheck :: currentPath
          traverse(valueExpr)
          currentPath = currentPath.tail

        case q"${ Ident(`thisPackage`) }.Derivation.Failed.apply[$typ]()" =>
          failures += typ.typecheck :: currentPath

        case expr => super.traverse(expr)
      }
    }

    //    println(show(tree))
    traverser.traverse(tree)
    failures.toList.distinct
  }

  private[this] def setImplicitNotFound[A: WeakTypeTag](failedDerivations: List[List[Tree]]): Unit = {
    val firstLine = failedDerivations match {
      case Nil => s"Could not find ${prettyPrintType(weakTypeOf[A])}"
      case _ => s"Could not derive ${prettyPrintType(weakTypeOf[A])}, because:"
    }

    val failedLines = failedDerivations.flatMap { chain =>
      chain.reverse.zipWithIndex.map {
        case (typ, i) if i == chain.length - 1 => s"${"  " * (i + 1)}- missing ${prettyPrintType(typ)}"
        case (typ, i) => s"${"  " * (i + 1)}- missing ${prettyPrintType(typ)}, because:"
      }
    }.mkString("\n")

    //    c.error(c.enclosingPosition, firstLine + "\n" + failedLines)
    setImplicitNotFound("\n" + firstLine + "\n" + failedLines)
  }

  private[this] def setImplicitNotFound(msg: String): Unit = {
    import c.internal.decorators._
    val infTree = c.typecheck(q"""new _root_.scala.annotation.implicitNotFound($msg)""", silent = false)
    typeOf[Derivation[_]].typeSymbol.setAnnotations(Annotation(infTree))
  }

  private[this] def prettyPrintType(typ: Type): String =
    prettyPrintType(typ.toTree)

  private[this] def prettyPrintType(typ: Tree): String = typ match {
    case tq"Lazy[$lzyArg]" => prettyPrintType(lzyArg) // ignore `Lazy` for the purposes of showing the implicit chain
    case tq"$typeclass[$arg]" => s"a $typeclass instance for type $arg"
    case _ => s"an implicit value of $typ"
  }
}
