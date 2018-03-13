package com.lsy.scala.util

import java.text.SimpleDateFormat
import java.util.Calendar
import com.github.nscala_time.time.Imports._

object TimeUtil {
  final val ONE_HOUR_MILLISECONDS = 60 * 60 * 1000

  final val SECOND_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

  final val DAY_DATE_FORMAT_ONE = "yyyy-MM-dd"

  final val DAY_DATE_FORMAT_TWO = "yyyyMMdd"

  def main(args: Array[String]): Unit = {
    val ts = 1520202612000L
    val startTime = "06:30:00"
    val endTime = "09:00:00"

    println(convertTimeStamp2Date(ts))
    var tsStr = convertTimeStamp2DateStr(ts, "HH:mm:ss")
    println(tsStr >= startTime & tsStr <= endTime)

    tsStr = "05:30:00"
    println(tsStr >= startTime & tsStr <= endTime)

    tsStr = "06:55:00"
    println(tsStr >= startTime & tsStr <= endTime)

    tsStr = "09:00:00"
    println(tsStr >= startTime & tsStr <= endTime)

    tsStr = "10:30:00"
    println(tsStr >= startTime & tsStr <= endTime)

  }

  //时间字符串=>时间戳
  def convertDateStr2TimeStamp(dateStr: String, pattern: String): Long = {
    new SimpleDateFormat(pattern).parse(dateStr).getTime
  }


  //时间字符串+天数=>时间戳
  def dateStrAddDays2TimeStamp(dateStr: String, pattern: String, days: Int): Long = {
    convertDateStr2Date(dateStr, pattern).plusDays(days).date.getTime
  }


  //时间字符串=>日期
  def convertDateStr2Date(dateStr: String, pattern: String): DateTime = {
    new DateTime(new SimpleDateFormat(pattern).parse(dateStr))
  }


  //时间戳=>日期
  def convertTimeStamp2Date(timestamp: Long): DateTime = {
    new DateTime(timestamp)
  }

  //时间戳=>字符串
  def convertTimeStamp2DateStr(timestamp: Long, pattern: String): String = {
    new DateTime(timestamp).toString(pattern)
  }

  //时间戳=>小时数
  def convertTimeStamp2Hour(timestamp: Long): String = {
    new DateTime(timestamp).hourOfDay().getAsString
  }


  //时间戳=>分钟数
  def convertTimeStamp2Minute(timestamp: Long): String = {
    new DateTime(timestamp).minuteOfHour().getAsString()
  }

  //时间戳=>秒数
  def convertTimeStamp2Sec(timestamp: Long): String = {
    new DateTime(timestamp).secondOfMinute().getAsString
  }



  def addZero(hourOrMin: String): String = {
    if (hourOrMin.toInt <= 9)
      "0" + hourOrMin
    else
      hourOrMin

  }

  def delZero(hourOrMin: String): String = {
    var res = hourOrMin
    if (!hourOrMin.equals("0") && hourOrMin.startsWith("0"))
      res = res.replaceAll("^0","")
    res
  }

  def dateStrPatternOne2Two(time: String): String = {
    convertTimeStamp2DateStr(convertDateStr2TimeStamp(time, DAY_DATE_FORMAT_ONE), DAY_DATE_FORMAT_TWO)
  }

  //获取星期几
  def dayOfWeek(dateStr: String): Int = {
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val date = sdf.parse(dateStr)

    //    val sdf2 = new SimpleDateFormat("EEEE")
    //    sdf2.format(date)

    val cal = Calendar.getInstance();
    cal.setTime(date);
    var w = cal.get(Calendar.DAY_OF_WEEK) - 1;

    //星期天 默认为0
    if (w <= 0)
      w = 7
    w
  }

  //判断是否是周末
  def isRestday(date: String): Boolean = {
    val dayNumOfWeek = dayOfWeek(date)
    dayNumOfWeek == 6 || dayNumOfWeek == 7
  }

}
