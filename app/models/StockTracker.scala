package models

import akka.actor.Actor
import play.api.libs.iteratee.Concurrent.Channel
import play.api.libs.json._
import scala.util.Random
import org.joda.time.DateTime

case object UpdateStocks

class StockTracker(stockChannel: Channel[JsValue]) extends Actor {
  
  var stockValue: Double = 20
  def stockDelta(): Double = {
    Random.nextInt(6) - Random.nextInt(3)
  }
  
  def receive: Receive = {
    case UpdateStocks => 
      val newValue = updateValue()
      val stockData = stock(newValue)
      println(stockData)
      stockChannel.push(stockData)
  }
  
  def updateValue(): Double = {
    stockValue += stockDelta()
    stockValue
  }
  
  def stock(value: Double) = JsObject (
    Seq (
      "price" -> JsNumber(value),
      "time" -> JsString(DateTime.now().toString())
    )  
  )
}