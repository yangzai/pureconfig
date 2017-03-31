package pureconfig

@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
trait NamingConvention {
  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def toTokens(s: String): Seq[String]
  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def fromTokens(l: Seq[String]): String
}

@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
trait CapitalizedWordsNamingConvention extends NamingConvention {
  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def toTokens(s: String): Seq[String] = {
    CapitalizedWordsNamingConvention.wordBreakPattern.split(s).map(_.toLowerCase)
  }
}

@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
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
@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
object CamelCase extends CapitalizedWordsNamingConvention {
  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
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
@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
object PascalCase extends CapitalizedWordsNamingConvention {
  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def fromTokens(l: Seq[String]): String = l.map(_.capitalize).mkString
}

@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
class StringDelimitedNamingConvention(d: String) extends NamingConvention {
  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def toTokens(s: String): Seq[String] =
    s.split(d).map(_.toLowerCase)

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def fromTokens(l: Seq[String]): String =
    l.map(_.toLowerCase).mkString(d)
}

/**
 * KebabCase identifiers look like `kebab-case` and `use-more-pureconfig`
 * @see http://wiki.c2.com/?KebabCase
 */
@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
object KebabCase extends StringDelimitedNamingConvention("-")

/**
 * SnakeCase identifiers look like `snake_case` and `use_more_pureconfig`
 * @see https://en.wikipedia.org/wiki/Snake_case
 */
@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
object SnakeCase extends StringDelimitedNamingConvention("_")
