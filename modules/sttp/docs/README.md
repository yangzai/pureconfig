# sttp module for PureConfig

Adds support for [sttp](https://github.com/softwaremill/sttp). Currently supports only `com.softwaremill.sttp.Uri`.

## Add pureconfig-sttp to your project

In addition to [core PureConfig](https://github.com/pureconfig/pureconfig), you'll need:

```scala
libraryDependencies += "com.github.pureconfig" %% "pureconfig-sttp" % "@VERSION@"
```

## Example

To load an sttp `Uri` into a configuration, create a new class:

```scala mdoc:silent
import sttp.model.Uri
import com.typesafe.config.ConfigFactory.parseString
import pureconfig._
import pureconfig.generic.auto._
import pureconfig.module.sttp._

case class AppConfig(uri: Uri)
```

Now, we can load the configuration with the following code:

```scala mdoc:to-string
val config = parseString("""{uri: "https://sttp.readthedocs.io" }""")

ConfigSource.fromConfig(config).load[AppConfig]
```
