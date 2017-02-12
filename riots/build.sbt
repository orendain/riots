import sbt.Project.projectToRef

lazy val clients = Seq(client)
lazy val scalaV = "2.11.7"

lazy val riots = (project in file("riots")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := clients,
  pipelineStages := Seq(scalaJSProd, gzip),
  libraryDependencies ++= Seq(
    filters,
    "com.vmunier" %% "play-scalajs-scripts" % "0.3.0",
    "com.lihaoyi" %% "upickle" % "0.3.6",
    "org.webjars" % "jquery" % "1.11.3",
    "org.webjars" % "jquery-colorbox" % "1.6.3",
    //"org.reactivemongo" %% "reactivemongo" % "0.11.9",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.11.9"
    //"org.reactivemongo" %% "play2-reactivemongo" % "0.11.2.play24"
  ),
  routesGenerator := InjectedRoutesGenerator
).enablePlugins(PlayScala).
  aggregate(clients.map(projectToRef): _*).
  dependsOn(riots_shared_server)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "upickle" % "0.3.6",
    "com.lihaoyi" %%% "scalatags" % "0.5.3",

    "org.scala-js" %%% "scalajs-dom" % "0.8.2",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.1"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(riots_shared_client)

lazy val riots_shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val riots_shared_server = riots_shared.jvm
lazy val riots_shared_client = riots_shared.js

// loads the jvm project at sbt startup
onLoad in Global := (Command.process("project riots", _: State)) compose (onLoad in Global).value
