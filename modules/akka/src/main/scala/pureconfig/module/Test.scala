package pureconfig.module

import pureconfig._
import shapeless._

trait MyTC[A]

object MyTC {
  def summon[A](implicit a: MyTC[A]) = a

  private def apply[A] = new MyTC[A] {}

  implicit val forInt = MyTC[Int]
  implicit val forString = MyTC[String]
  implicit val forBool = MyTC[Boolean]

  implicit def forOpt[A](implicit a: Derivation[MyTC[A]]): MyTC[Option[A]] = MyTC[Option[A]]
  implicit def forList[A](implicit a: Derivation[MyTC[A]]): MyTC[List[A]] = MyTC[List[A]]
  implicit def forMap[K, V](implicit a: Derivation[MyTC[K]], b: Derivation[MyTC[V]]): MyTC[Map[K, V]] = MyTC[Map[K, V]]

  implicit def forHNil: MyTC[HNil] = MyTC[HNil]
  implicit def forHCons[H, T <: HList](implicit a: Derivation[MyTC[H]], b: MyTC[T]): MyTC[H :: T] = MyTC[H :: T]
  implicit def forCNil: MyTC[CNil] = MyTC[CNil]
  implicit def forCCons[H, T <: Coproduct](implicit a: Derivation[MyTC[H]], b: MyTC[T]): MyTC[H :+: T] = MyTC[H :+: T]

  implicit def forGen[F, Repr](implicit gen: Generic.Aux[F, Repr], cc: MyTC[Repr]): MyTC[F] = MyTC[F]
}

object MainTC {
  class NoRead(a: Int, b: Int)
  class NoRead2(a: Int, b: Int)

  sealed trait Conf
  case class Conf1(a: Int) extends Conf
  case class Conf2(b: String) extends Conf
  case class Conf3(b: Option[NoRead]) extends Conf

  //  implicitly[Derivation[MyTC[Option[NoRead]]]]
  //  implicitly[Derivation[MyTC[Option[NoRead] :: HNil]]]
  implicitly[Derivation[MyTC[Conf3]]]

  //  implicitly[Derivation[ConfigReader[Option[NoRead] :: HNil]]]
  //  implicitly[Derivation[ConfigReader[List[Option[NoRead]]]]]
}
