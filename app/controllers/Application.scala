package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.Enumerator
import play.api.libs.EventSource
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def events = Action {
    val numbers = Seq(1, 2, 3, 4, 5).map(JsNumber(_))
    val events = Enumerator.enumerate[JsValue](numbers) &> EventSource[JsValue]()
    Ok.stream(events).as("text/event-stream")
  }

}
