/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package pureconfig.error

/**
 * A non-empty list of ConfigReader failures
 */
@pureconfig.deprecated
case class ConfigReaderFailures(head: ConfigReaderFailure, tail: List[ConfigReaderFailure]) {

  @pureconfig.deprecated
  def toList: List[ConfigReaderFailure] = head +: tail

  @pureconfig.deprecated
  def +:(failure: ConfigReaderFailure): ConfigReaderFailures =
    new ConfigReaderFailures(failure, this.toList)

  @pureconfig.deprecated
  def ++(that: ConfigReaderFailures): ConfigReaderFailures =
    new ConfigReaderFailures(head, tail ++ that.toList)
}

@pureconfig.deprecated
object ConfigReaderFailures {

  @pureconfig.deprecated
  def apply(configReaderFailure: ConfigReaderFailure): ConfigReaderFailures =
    new ConfigReaderFailures(configReaderFailure, List.empty[ConfigReaderFailure])
}
