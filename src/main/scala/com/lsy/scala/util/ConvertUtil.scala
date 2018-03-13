package com.lsy.scala.util

import org.slf4j.LoggerFactory

case class FlowCycle(junctionId: String,
                     startTimeSlice: String,
                     endTimeSlice: String,
                     estimateCycle: String,
                     variationCoefficient: String,
                     phaseNumber: Int,
                     trailNumber: Int)

object ConvertUtil {

  val logger = LoggerFactory.getLogger(this.getClass)

  def parseFlowCycleData(line: String): Option[FlowCycle] = {
    line.split(" ") match {
      case Array(
      _,
      junctionId,
      timeSlice,
      estimateCycle,
      variationCoefficient,
      phaseNumber,
      trailNumber
      ) => try {
        Some(FlowCycle(junctionId,
          timeSlice.split("-")(0),
          timeSlice.split("-")(1),
          estimateCycle,
          variationCoefficient,
          phaseNumber.toInt,
          trailNumber.toInt
        ))
      } catch {
        case _: Exception => {
          logger.warn(s"data format is not right after split with tab: ${line} ")
          None
        }
      }
      case _ => {
        logger.warn(s"data format is not right after split with tab: ${line} ")
        None
      }
    }
  }

  def main(args: Array[String]) = {
    println(parseFlowCycleData("0171122 10935735 00:00:00-00:30:00 80 0.353913 1 386"))
  }

}
