package pureconfig.error

@scala.deprecated(
  message = "The pureconfig artifact with organization com.github.melrief is deprecated and won't be published anymore. Please update your dependency to use the organization com.github.pureconfig",
  since = "0.7.0")
case class CollidingKeysException(key: String, existingValue: String)
  extends IllegalArgumentException(s"The coproduct hint key '$key' collides with existing field '$existingValue'")
