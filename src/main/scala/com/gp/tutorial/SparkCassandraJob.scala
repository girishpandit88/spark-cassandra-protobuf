package com.gp.tutorial

import com.datastax.spark.connector.cql.CassandraConnector
import com.gp.tutorial.student.Student
import org.apache.spark.SparkConf
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}
import scalapb.spark.ProtoSQL

object SparkCassandraJob {
  def parseLine(s: String): Student =
    Student
      .parseFrom(
        org.apache.commons.codec.binary.Base64
          .decodeBase64(s)
      )

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
      .set("spark.cassandra.connection.host", "127.0.0.1")
      .set("spark.cassandra.auth.username", "cassandra")
      .set("spark.cassandra.auth.password", "cassandra")
    val spark = SparkSession
      .builder()
      .appName("sparkC*Job")
      .master("local")
      .config(conf)
      .getOrCreate()
    val sc = spark.sparkContext
    val cassandraConnector = CassandraConnector(sc.getConf)
    val students = sc
      .textFile(
        SparkCassandraJob.getClass
          .getResource("/input.txt")
          .getPath
      )
      .map(parseLine)
    import scalapb.spark.Implicits._
    val personsDS1: Dataset[Array[Byte]] = spark
      .createDataset(
        students
          .map(_.toByteArray)
          .collect()
      )

    import org.apache.spark.sql.functions._
    val persist = personsDS1
      .withColumn("student_id", monotonically_increasing_id)
      .withColumn(
        "student",
        personsDS1
          .col("_1")
      )
      .drop("_1")
    persist
      .show(10)
    persist.write
      .mode(SaveMode.Append)
      .format("org.apache.spark.sql.cassandra")
      .options(Map("keyspace" -> "tutorialspoint", "table" -> "student"))
      .save()

    val df = spark.read
      .format("org.apache.spark.sql.cassandra")
      .options(Map("keyspace" -> "tutorialspoint", "table" -> "student"))
      .load
    df.printSchema()
    val parseData = ProtoSQL
      .udf { bytes: Array[Byte] =>
        Student
          .parseFrom(bytes)
      }
    import spark.implicits.StringToColumn
    val parsedDf = df
      .withColumn("parsed", parseData($"student"))
    parsedDf
      .printSchema()
    parsedDf
      .show(20, truncate = false)
  }

}
