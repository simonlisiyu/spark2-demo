package com.lsy.spark.io

import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapred.{FileSplit, InputSplit, TextInputFormat}
import org.apache.spark.rdd.HadoopRDD
import org.apache.spark.sql.{DataFrame, Dataset, SaveMode, SparkSession}

/**
  * spark to hdfs input and output
  */
object Spark2HdfsHiveIO {
  def main(args: Array[String]): Unit = {

    // init spark session
    val spark = SparkSession.builder().appName(s"CityOrderGpsData").enableHiveSupport().getOrCreate()

    val ds = readDatasetStrFromHdfs(spark, "/user/lsy/test")
    ds
    val df = executeSqlFromHive(spark, "mydb", "SELECT * FROM mytable")
    val dff = executeSqlFromHive(spark, "mydb", "INSERT OVERWRITE TABLE mytable PARTITION (city_id=12, dt='20180202")
    df
    dff

    val save = writeDataFrameToHdfs(spark, df, "/user/lsy/test1")
    save

    // stop spark session
    spark.stop()
  }

  /**
    * read hdfs file, return Dataset[String]
    * @param spark
    * @param path
    * @return
    */
  def readDatasetStrFromHdfs(spark:SparkSession, path:String):Dataset[String] = {

    // hadoop rdd
    val fileRdd = spark.sparkContext.hadoopFile[LongWritable, Text, TextInputFormat](path)
    val hadoopRdd = fileRdd.asInstanceOf[HadoopRDD[LongWritable, Text]]
    val junctionFlowDS = hadoopRdd.mapPartitionsWithInputSplit((inputSplit: InputSplit, iterator: Iterator[(LongWritable, Text)]) => {
      val filePath = inputSplit.asInstanceOf[FileSplit]
      iterator.map(line => (filePath.getPath.toString, line._2.toString))
    }).filter(item => parseVersionAndCityId(item._1) match {
      case Some((_: String, cityId: Int)) => true
      case None => false
    })
      .flatMap(item => item._1.split(","))
    junctionFlowDS

    // method 1
    spark.read.textFile(path).repartition(100)
  }

  /**
    * read hive table, return DataFrame
    * @param spark
    * @param db
    * @param sql
    * @return
    */
  def executeSqlFromHive(spark:SparkSession, db:String, sql:String) = {
    spark.sql(s"use ${db}")
    spark.sql(sql)
  }

  /**
    * write DataFrame to hdfs
    * @param spark
    * @param df
    * @param path
    */
  def writeDataFrameToHdfs(spark:SparkSession, df:DataFrame, path:String):Unit = {
    import spark.implicits._
    df.map(_.mkString("\t")).rdd.repartition(1).saveAsTextFile(path)
    df.repartition(10).map(_.mkString("\t")).write.mode(SaveMode.Overwrite).text(path)
  }

  def writeDataFrameToHive(spark:SparkSession, df:DataFrame, saveMode:SaveMode, db:String, table:String) = {
    spark.sql(s"use ${db}")
    spark.sql("SET hive.exec.dynamic.partition = true")
    spark.sql("SET hive.exec.dynamic.partition.mode = nonstrict ")
    spark.sql("SET hive.exec.max.dynamic.partitions.pernode = 400")
    df.repartition(10).write.mode(saveMode).insertInto(table)
  }

  implicit def stringToInt(tempString: String): Int = Integer.parseInt(tempString)

  implicit def stringToBigInt(tempString: String): BigInt = BigInt(tempString)

  def parseVersionAndCityId(filePath: String): Option[(String, Int)] = {
    val regex = """.*\/([0-9]{10,10})\/flow_city\/flow_city_([0-9]+)_hdfs""".r
    filePath match {
      case regex(mapVersion, cityId) => Some(Tuple2(mapVersion, cityId))
      case _ => None
    }
  }


}
