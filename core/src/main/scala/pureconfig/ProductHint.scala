package pureconfig

/**
 * A trait that can be implemented to customize how case classes are read from and written to a config.
 *
 * @tparam T the type of case class for which this hint applies
 */
@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
trait ProductHint[T] {

  /**
   * Returns the key in the config object associated with a given case class field.
   *
   * @param fieldName the case class field
   * @return the key in the config object associated with the given case class field.
   */
  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def configKey(fieldName: String): String

  /**
   * A boolean indicating if the default arguments of the case class should be used when fields are missing
   */
  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def useDefaultArgs: Boolean

  /**
   * A boolean indicating if config keys that do not map to a case class field are allowed in config objects
   */
  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def allowUnknownKeys: Boolean
}

private[pureconfig] case class ProductHintImpl[T](
    fieldMapping: ConfigFieldMapping,
    useDefaultArgs: Boolean,
    allowUnknownKeys: Boolean) extends ProductHint[T] {

  def configKey(fieldName: String) = fieldMapping(fieldName)
}

@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
object ProductHint {

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def apply[T](
    fieldMapping: ConfigFieldMapping = ConfigFieldMapping(CamelCase, KebabCase),
    useDefaultArgs: Boolean = true,
    allowUnknownKeys: Boolean = true): ProductHint[T] =
    ProductHintImpl[T](fieldMapping, useDefaultArgs, allowUnknownKeys)

  implicit def default[T]: ProductHint[T] = apply()
}
