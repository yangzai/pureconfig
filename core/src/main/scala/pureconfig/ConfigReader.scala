package pureconfig

import scala.reflect.ClassTag
import scala.util.Try

import com.typesafe.config.ConfigValue
import pureconfig.ConvertHelpers._
import pureconfig.error.{ ConfigReaderFailure, ConfigReaderFailures, ConfigValueLocation }

/**
 * Trait for objects capable of reading objects of a given type from `ConfigValues`.
 *
 * @tparam T the type of objects readable by this `ConfigReader`
 */
@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
trait ConfigReader[T] {

  /**
   * Convert the given configuration into an instance of `T` if possible.
   *
   * @param config The configuration from which load the config
   * @return either a list of failures or an object of type `T`
   */
  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def from(config: ConfigValue): Either[ConfigReaderFailures, T]
}

/**
 * Provides methods to create [[ConfigReader]] instances.
 */
@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
object ConfigReader extends BasicReaders with DerivedReaders {

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def apply[T](implicit reader: ConfigReader[T]): ConfigReader[T] = reader

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def fromString[T](fromF: String => Option[ConfigValueLocation] => Either[ConfigReaderFailure, T]): ConfigReader[T] = new ConfigReader[T] {
    override def from(config: ConfigValue): Either[ConfigReaderFailures, T] = stringToEitherConvert(fromF)(config)
  }

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def fromStringTry[T](fromF: String => Try[T])(implicit ct: ClassTag[T]): ConfigReader[T] = {
    fromString[T](tryF(fromF))
  }

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def fromStringOpt[T](fromF: String => Option[T])(implicit ct: ClassTag[T]): ConfigReader[T] = {
    fromString[T](optF(fromF))
  }

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def fromNonEmptyString[T](fromF: String => Option[ConfigValueLocation] => Either[ConfigReaderFailure, T])(implicit ct: ClassTag[T]): ConfigReader[T] = {
    fromString(string => location => ensureNonEmpty(ct)(string)(location).right.flatMap(s => fromF(s)(location)))
  }

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def fromNonEmptyStringTry[T](fromF: String => Try[T])(implicit ct: ClassTag[T]): ConfigReader[T] = {
    fromNonEmptyString[T](tryF(fromF))
  }

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def fromNonEmptyStringOpt[T](fromF: String => Option[T])(implicit ct: ClassTag[T]): ConfigReader[T] = {
    fromNonEmptyString[T](optF(fromF))
  }
}
