package pureconfig

@pureconfig.deprecated
trait NamingConvention {
  @pureconfig.deprecated
  def toTokens(s: String): Seq[String]
  @pureconfig.deprecated
  def fromTokens(l: Seq[String]): String
}

@pureconfig.deprecated
trait CapitalizedWordsNamingConvention extends NamingConvention {
  @pureconfig.deprecated
  def toTokens(s: String): Seq[String] = {
    CapitalizedWordsNamingConvention.wordBreakPattern.split(s).map(_.toLowerCase)
  }
}

@pureconfig.deprecated
object CapitalizedWordsNamingConvention {
  private val wordBreakPattern = String.format(
    "%s|%s|%s",
    "(?<=[A-Z])(?=[A-Z][a-z])",
    "(?<=[^A-Z])(?=[A-Z])",
    "(?<=[A-Za-z])(?=[^A-Za-z])").r
}

/**
 * CamelCase identifiers look like `camelCase` and `useMorePureconfig`
 * @see https://en.wikipedia.org/wiki/Camel_case
 */
@pureconfig.deprecated
object CamelCase extends CapitalizedWordsNamingConvention {
  @pureconfig.deprecated
  def fromTokens(l: Seq[String]): String = {
    l match {
      case Seq() => ""
      case h +: Seq() => h.toLowerCase
      case h +: t => h.toLowerCase + t.map(_.capitalize).mkString
    }
  }
}

/**
 * PascalCase identifiers look like e.g.`PascalCase` and `UseMorePureconfig`
 * @see https://en.wikipedia.org/wiki/PascalCase
 */
@pureconfig.deprecated
object PascalCase extends CapitalizedWordsNamingConvention {
  @pureconfig.deprecated
  def fromTokens(l: Seq[String]): String = l.map(_.capitalize).mkString
}

@pureconfig.deprecated
class StringDelimitedNamingConvention(d: String) extends NamingConvention {
  @pureconfig.deprecated
  def toTokens(s: String): Seq[String] =
    s.split(d).map(_.toLowerCase)

  @pureconfig.deprecated
  def fromTokens(l: Seq[String]): String =
    l.map(_.toLowerCase).mkString(d)
}

/**
 * KebabCase identifiers look like `kebab-case` and `use-more-pureconfig`
 * @see http://wiki.c2.com/?KebabCase
 */
@pureconfig.deprecated
object KebabCase extends StringDelimitedNamingConvention("-")

/**
 * SnakeCase identifiers look like `snake_case` and `use_more_pureconfig`
 * @see https://en.wikipedia.org/wiki/Snake_case
 */
@pureconfig.deprecated
object SnakeCase extends StringDelimitedNamingConvention("_")
