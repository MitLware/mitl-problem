name := "mitl-problem"

version := "0.1.0"

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:existentials", "-language:higherKinds")    

javacOptions in doc ++= Seq("-source", "1.5")

publishArtifact in (Compile, packageDoc) := false

publishMavenStyle := true

sources in (Compile,doc) := Seq.empty

scalaVersion := "2.12.0"


libraryDependencies += "default" % "mitl-support_2.12" % "0.1.0"

libraryDependencies += "default" % "mitl-solution_2.12" % "0.1.0"

// https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.1"

// https://mvnrepository.com/artifact/org.json/json
libraryDependencies += "org.json" % "json" % "20170516"

// https://mvnrepository.com/artifact/junit/junit
libraryDependencies += "junit" % "junit" % "4.12"

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test "


licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
