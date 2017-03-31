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
@pureconfig.deprecated
trait ConfigReader[T] {

  /**
   * Convert the given configuration into an instance of `T` if possible.
   *
   * @param config The configuration from which load the config
   * @return either a list of failures or an object of type `T`
   */
  @pureconfig.deprecated
  def from(config: ConfigValue): Either[ConfigReaderFailures, T]
}

/**
 * Provides methods to create [[ConfigReader]] instances.
 */
@pureconfig.deprecated
object ConfigReader extends BasicReaders with DerivedReaders {

  @pureconfig.deprecated
  def apply[T](implicit reader: ConfigReader[T]): ConfigReader[T] = reader

  @pureconfig.deprecated
  def fromString[T](fromF: String => Option[ConfigValueLocation] => Either[ConfigReaderFailure, T]): ConfigReader[T] = new ConfigReader[T] {
    override def from(config: ConfigValue): Either[ConfigReaderFailures, T] = stringToEitherConvert(fromF)(config)
  }

  @pureconfig.deprecated
  def fromStringTry[T](fromF: String => Try[T])(implicit ct: ClassTag[T]): ConfigReader[T] = {
    fromString[T](tryF(fromF))
  }

  @pureconfig.deprecated
  def fromStringOpt[T](fromF: String => Option[T])(implicit ct: ClassTag[T]): ConfigReader[T] = {
    fromString[T](optF(fromF))
  }

  @pureconfig.deprecated
  def fromNonEmptyString[T](fromF: String => Option[ConfigValueLocation] => Either[ConfigReaderFailure, T])(implicit ct: ClassTag[T]): ConfigReader[T] = {
    fromString(string => location => ensureNonEmpty(ct)(string)(location).right.flatMap(s => fromF(s)(location)))
  }

  @pureconfig.deprecated
  def fromNonEmptyStringTry[T](fromF: String => Try[T])(implicit ct: ClassTag[T]): ConfigReader[T] = {
    fromNonEmptyString[T](tryF(fromF))
  }

  @pureconfig.deprecated
  def fromNonEmptyStringOpt[T](fromF: String => Option[T])(implicit ct: ClassTag[T]): ConfigReader[T] = {
    fromNonEmptyString[T](optF(fromF))
  }
}
