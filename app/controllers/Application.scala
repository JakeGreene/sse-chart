package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.Enumerator
import play.api.libs.iteratee.Concurrent
import play.api.libs.EventSource
import play.api.libs.json._
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current
import models.StockTracker
import akka.actor.Props
import models.UpdateStocks
import play.api.libs.concurrent.Akka

class Application extends Controller {
  
  val (stockFeed, stockChannel) = Concurrent.broadcast[JsValue]
  val tracker = Akka.system.actorOf(Props(classOf[StockTracker], stockChannel), "stock-tracker")
  Akka.system.scheduler.schedule(1.second, 3.seconds)(tracker ! UpdateStocks)

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def events = Action {
    Ok.stream(stockFeed through EventSource()).as("text/event-stream")
  }

}
