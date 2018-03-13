package com.lsy.scala.thread

import java.util.concurrent.TimeUnit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object FutureTest {

  def computation(): Int = { 25 + 50 }
  val theFuture = Future { computation() }

  def main(args: Array[String]): Unit = {
    val f1 = Future {
      TimeUnit.SECONDS.sleep(1)
      "f1"
    }
    val f2 = Future {
      TimeUnit.SECONDS.sleep(2)
      "f2"
    }
    val f3 = Future {
      TimeUnit.SECONDS.sleep(3)
      2342
    }
    val f4 = Future.sequence(Seq(f1, f2, f3))
    val results: Seq[Any] = Await.result(f4, Duration.Inf)
    println(results) // 输出：List(f1, f2, 2342)

    val f5: Future[(String, String, Int)] =
      for {
        r2 <- f2
        r3 <- f3
        r1 <- f1
      } yield (r1.take(1), r2.drop(1), r3 + 1)
    val (f1Str, f2Str, f3Int) = Await.result(f5, Duration.Inf)
    println(s"f1: $f1Str, f2: $f2Str, f3: $f3Int") // 输出：f1: f, f2: 2, f3: 2342



    //    theFuture.onComplate {
//      case Success(result) => println(result)
//      case Fialure(t) => println(s"Error: %{t.getMessage}")
//    }

  }

}
