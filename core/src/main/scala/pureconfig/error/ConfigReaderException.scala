/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pureconfig.error

import scala.collection.mutable
import scala.reflect.ClassTag

@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
final case class ConfigReaderException[T](failures: ConfigReaderFailures)(implicit ct: ClassTag[T]) extends RuntimeException {

  override def getMessage: String = {
    val linesBuffer = mutable.Buffer.empty[String]
    linesBuffer += s"Cannot convert configuration to a ${ct.runtimeClass.getName}. Failures are:"

    val failuresByPath = failures.toList.groupBy(_.path)
    val failuresWithPath = (failuresByPath - None).map({ case (k, v) => k.get -> v }).toList.sortBy(_._1)
    val failuresWithoutPath = failuresByPath.getOrElse(None, Nil)

    if (failuresWithoutPath.nonEmpty)
      linesBuffer += "  in the configuration:"

    failuresWithoutPath.foreach { failure =>
      linesBuffer += s"    - ${ConfigReaderException.descriptionWithLocation(failure)}"
    }

    if (failuresWithPath.nonEmpty && failuresWithoutPath.nonEmpty) {
      linesBuffer += ""
    }

    failuresWithPath.foreach {
      case (p, failures) =>
        linesBuffer += s"  at '$p':"
        failures.foreach { failure =>
          linesBuffer += s"    - ${ConfigReaderException.descriptionWithLocation(failure)}"
        }
    }

    linesBuffer += ""
    linesBuffer.mkString(System.lineSeparator())
  }

}

@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
object ConfigReaderException {
  private[ConfigReaderException] def descriptionWithLocation(failure: ConfigReaderFailure): String =
    failure.location.fold(failure.description)(_.description + " " + failure.description)
}
