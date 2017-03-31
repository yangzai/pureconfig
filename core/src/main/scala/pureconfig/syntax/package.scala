package pureconfig

import com.typesafe.config.{ ConfigValue, Config => TypesafeConfig }
import pureconfig.error.{ ConfigReaderException, ConfigReaderFailures }

import scala.reflect.ClassTag

package object syntax {
  implicit class PimpedAny[T](val any: T) extends AnyVal {
    @scala.deprecated(
      message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
      since = "0.7.0")
    def toConfig(implicit writer: ConfigWriter[T]): ConfigValue = writer.to(any)
  }

  private def getResultOrThrow[Config](failuresOrResult: Either[ConfigReaderFailures, Config])(implicit ct: ClassTag[Config]): Config = {
    failuresOrResult match {
      case Right(config) => config
      case Left(failures) => throw new ConfigReaderException[Config](failures)
    }
  }

  implicit class PimpedConfigValue(val conf: ConfigValue) extends AnyVal {
    @scala.deprecated(
      message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
      since = "0.7.0")
    def to[T](implicit reader: ConfigReader[T]): Either[ConfigReaderFailures, T] = reader.from(conf)
    @scala.deprecated(
      message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
      since = "0.7.0")
    def toOrThrow[T](implicit reader: ConfigReader[T], cl: ClassTag[T]): T = getResultOrThrow(reader.from(conf))(cl)
  }

  implicit class PimpedConfig(val conf: TypesafeConfig) extends AnyVal {
    @scala.deprecated(
      message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
      since = "0.7.0")
    def to[T: ConfigReader]: Either[ConfigReaderFailures, T] = conf.root().to[T]
    @scala.deprecated(
      message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
      since = "0.7.0")
    def toOrThrow[T](implicit reader: ConfigReader[T], cl: ClassTag[T]): T = getResultOrThrow(conf.root().to[T])(cl)
  }
}
