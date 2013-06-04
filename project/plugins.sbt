//resolvers += "Web plugin repo" at "http://siasia.github.com/maven2"    
addSbtPlugin("com.earldouglas" % "xsbt-web-plugin" % "0.3.0")
//Following means libraryDependencies += "com.github.siasia" %% "xsbt-web-plugin" % "0.1.1-<sbt version>""
//libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v+"-0.2.9"))

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.2.0")