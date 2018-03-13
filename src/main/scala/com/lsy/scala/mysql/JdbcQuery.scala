package com.lsy.scala.mysql

object JdbcQuery {

  def main(args: Array[String]): Unit = {
    import java.sql._

    Class.forName("org.h2.Driver")
    val conn = DriverManager.getConnection("jdbc:h2:mem:test1")
    val people = new scala.collection.mutable.MutableList[(Int,String,Int)]()
    try{
      val stmt = conn.createStatement()
      try{

        val rs = stmt.executeQuery("select ID, NAME, AGE from PERSON")
        try{
          while(rs.next()){
            people += ((rs.getInt(1), rs.getString(2), rs.getInt(3)))
          }
        }finally{
          rs.close()
        }

      }finally{
        stmt.close()
      }
    }finally{
      conn.close()
    }
  }
}
