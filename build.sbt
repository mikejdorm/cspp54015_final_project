// see https://github.com/siasia/xsbt-web-plugin for more information on the
// jetty plugin

// import web settings
seq(webSettings :_*)

name := "cspp_rest_service"

version := "1.0"

scalaVersion := "2.10.1"

// scalaVersion := "2.10.1-local"

// autoScalaLibrary := false

// scalaHome := Some(file("/Users/michaeldorman/scala-2.10.1/"))

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "org.eclipse.jetty.orbit"   % "javax.servlet"   % "3.0.0.v201112011016" % "provided"    artifacts Artifact("javax.servlet", "jar", "jar"),
    "org.eclipse.jetty"         % "jetty-webapp"    % "8.1.5.v20120716"     % "container"
)

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.1"

resolvers += "repo.codahale.com" at "http://repo.codahale.com"
 
libraryDependencies += "com.codahale" % "jerkson_2.9.1" % "0.5.0"

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies += "io.spray" %%  "spray-json" % "1.2.5"

libraryDependencies += "org.json4s" %% "json4s-native" % "3.2.2"

libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.2"