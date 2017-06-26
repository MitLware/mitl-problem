lazy val root = (project in file(".")).
  settings(
    name := "problems",
    version := "1.0",
    scalaVersion := "2.11.8"
  )

// https://mvnrepository.com/artifact/org.apache.commons/commons-lang3
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.1"

// https://mvnrepository.com/artifact/org.json/json
libraryDependencies += "org.json" % "json" % "20170516"

// https://mvnrepository.com/artifact/junit/junit
libraryDependencies += "junit" % "junit" % "4.12"

libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test"
