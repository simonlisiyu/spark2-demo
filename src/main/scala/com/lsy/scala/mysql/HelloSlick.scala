package com.lsy.scala.mysql

import slick.driver.MySQLDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


object HelloSlick extends App {
  val db = Database.forConfig("mysql")
  try {

    val suppliers: TableQuery[Suppliers] = TableQuery[Suppliers]
    val coffees: TableQuery[Coffees] = TableQuery[Coffees]

    val setup = DBIO.seq(
      (suppliers.schema ++ coffees.schema).create,
      suppliers +=(101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199"),
      suppliers +=(49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460"),
      suppliers +=(150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966")
    )
    val setupFuture: Future[Unit] = db.run(setup)

    val f = setupFuture.flatMap { _ =>
      val insertAction: DBIO[Option[Int]] = coffees ++= Seq(
        (12, "Colombian", 101, 7.99, 0, 0),
        (22, "French_Roast", 49, 8.99, 0, 0),
        (11, "Espresso", 150, 9.99, 0, 0),
        (1, "Colombian_Decaf", 101, 8.99, 0, 0),
        (32, "French_Roast_Decaf", 49, 9.99, 0, 0)
      )
      val insertAndPrintAction: DBIO[Unit] = insertAction.map { coffeesInsertResult =>
        // Print the number of rows inserted
        coffeesInsertResult foreach { numRows =>
          println(s"Inserted $numRows rows into the Coffees table")
        }
      }

      val allSuppliersAction: DBIO[Seq[(Int, String, String, String, String, String)]] = suppliers.result

      val combinedAction: DBIO[Seq[(Int, String, String, String, String, String)]] = insertAndPrintAction >> allSuppliersAction

      val combinedFuture: Future[Seq[(Int, String, String, String, String, String)]] = db.run(combinedAction)

      combinedFuture.map { allSuppliers =>
        allSuppliers.foreach(println)
      }
    }.flatMap{  _ =>
      //打印数据
      db.run(coffees.result).map{ all =>
        all.foreach{
          case (id, name, supID, price, sales, total) => println(" " +id + "\t" + name + "\t" + supID + "\t" + price + "\t" + sales + "\t" + total)
          case x => println(x)
        }
      }
    }.flatMap{ _ =>
      println("===========================")

      val q1 = for{c <- coffees} yield LiteralColumn("  ") ++ c.name ++ "\t" ++ c.supID.asColumnOf[String] ++
        "\t" ++ c.price.asColumnOf[String] ++ "\t" ++ c.sales.asColumnOf[String] ++ "\t" ++ c.total.asColumnOf[String]
      db.stream(q1.result).foreach{println}

    }
    Await.result(f, Duration.Inf)
  } finally db.close

}
