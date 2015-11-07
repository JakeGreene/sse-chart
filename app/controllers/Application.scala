package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.Enumerator
import play.api.libs.EventSource
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Application extends Controller {
  
  val numbers = Seq(1, 2, 3, 4, 5).map(JsNumber(_))
  val numericEvents = Enumerator.enumerate[JsValue](numbers) &> EventSource()

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def events = Action {
    Ok.stream(numericEvents).as("text/event-stream")
  }

}
