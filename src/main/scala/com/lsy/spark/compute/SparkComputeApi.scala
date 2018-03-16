package com.lsy.spark.compute

import org.apache.spark.sql.{DataFrame, Encoders, SparkSession}

object SparkComputeApi {
  /**
    * Spark 算子说明
    * http://lxw1234.com/archives/2015/07/363.htm
    */


  /**
    * flat map + 隐式转换
    * @param spark
    * @param dataFrame
    * @return
    */
  def flatMapWithImplicit(spark: SparkSession, dataFrame: DataFrame) ={
    import spark.implicits._
    implicit val matchError = org.apache.spark.sql.Encoders.tuple( Encoders.STRING,
      Encoders.STRING, Encoders.STRING, Encoders.STRING, Encoders.INT)
    val stepFourDF = dataFrame.flatMap(item => {
      val array = Array("1", "2", "3")
      array.map(cell_id => (item.getAs[String]("id"),
        item.getAs[String]("name"),
        item.getAs[String]("a_id")+","+item.getAs[String]("time")+","+item.getAs[String]("b_id"),
        cell_id,
        array.length
      )
      )
    })
    stepFourDF.toDF("id", "name", "id_time", "cell_id", "length")
  }
}
