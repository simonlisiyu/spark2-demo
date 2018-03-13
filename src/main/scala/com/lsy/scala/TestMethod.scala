package com.lsy.scala

import java.text.SimpleDateFormat
import java.util.Locale


object TestMethod {

  def main(args: Array[String]): Unit = {
    getSignalTimingList(12, "20180212", in_link="2222", out_link="2222")
    println(System.currentTimeMillis())

    val ts = Array(2,4,-5,-1,-10)
    val t_sec_step = 5
    for(t <- ts){
      println(if(t >= 0) t - t%t_sec_step else t - t%t_sec_step - t_sec_step)
    }

    val loc = new Locale("en")
    val fm = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss",loc)
    val tm = "30/Jul/2015:05:00:50"
    val dt2 = fm.parse(tm);
    println(dt2.getTime()/1000)

    var map:scala.collection.mutable.Map[String,Int] = scala.collection.mutable.Map()
    map += ("aaa" -> 111)
    map += ("bbb" -> 222)
    println(map.getOrElse("bbb", 0))
    println(map.size)

    val list = 1 to 10
//    println(list.indexOf(list.takeRight(1)(0)))
//    println(list.indexOf(10))
//    println((for(i <- list.slice(1,3)) yield i).sum)
//    println((for(i <- list.dropRight(10)) yield i).sum)
//    println(list.dropRight(10))
    println(list.tail)
    var d = 0
    for (num <- list if(d == 0)){
      println("num="+num)
      d = if(num>6) num else 0
    }
    println("d="+d)

    val array = Array("aaa","bbb")
    println(array.foreach(map.getOrElse(_, 0)))

    var before_stop_count_flag = "0"
    before_stop_count_flag += "1"
    var before_stop_count = 0
    if(before_stop_count_flag.length == 2){
      if(before_stop_count_flag == "11") {before_stop_count = 1;before_stop_count_flag=""}
      else before_stop_count_flag = before_stop_count_flag.drop(1)
    }
    println(before_stop_count_flag)
    println(before_stop_count)

//    Array("111","222","111").foreach(print)
    (Array("111","222","111").toSet - "111").foreach(print)

    val a = Array(Array("111","222","111"), Array("aaa","bbb","ccc"))
    val b = a.flatMap(x => x)
    b.foreach(println)

  }

  def getSignalTimingList(city_id: Int, stat_date: String, junction_id: String = "*",
                          start_time: String = "00:00:00", end_time: String = "00:00:00",
                          in_link: String = "*", out_link: String = "*") = {
    println("ddd")
  }
}
