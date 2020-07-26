name := "scala-cassandra-protobuf"

version := "0.1"

scalaVersion := "2.12.4"


val sparkVersion = "2.4.6"
val sqlVersion = "2.4.6"

libraryDependencies ++= Seq(
	"org.apache.spark" % "spark-core_2.12" % "2.4.6",
	"org.apache.spark" % "spark-sql_2.12" % "2.4.6",
	"com.datastax.spark" % "spark-cassandra-connector_2.12" % "2.5.1",
	"com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
	"com.thesamet.scalapb" %% "sparksql-scalapb" % "0.10.4"
)

PB.targets in Compile := Seq(
	scalapb.gen() -> (sourceManaged in Compile).value / "scalapb"
)

