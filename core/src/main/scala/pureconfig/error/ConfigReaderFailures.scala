/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pureconfig.error

/**
 * A non-empty list of ConfigReader failures
 */
@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
case class ConfigReaderFailures(head: ConfigReaderFailure, tail: List[ConfigReaderFailure]) {

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def toList: List[ConfigReaderFailure] = head +: tail

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def +:(failure: ConfigReaderFailure): ConfigReaderFailures =
    new ConfigReaderFailures(failure, this.toList)

  @scala.deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def ++(that: ConfigReaderFailures): ConfigReaderFailures =
    new ConfigReaderFailures(head, tail ++ that.toList)
}

@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
object ConfigReaderFailures {

  @deprecated(
    message = "The pureconfig artifact with organization com.github.melrief is deprecatedand won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
    since = "0.7.0")
  def apply(configReaderFailure: ConfigReaderFailure): ConfigReaderFailures =
    new ConfigReaderFailures(configReaderFailure, List.empty[ConfigReaderFailure])
}
