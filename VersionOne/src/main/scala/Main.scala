import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import akka.stream.{ ActorMaterializer, Materializer }
import akka.util.ByteString

import scala.io.StdIn

import Directives._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

object FoodOrder {

  case class OrderDesc(name: String, quantity: Int)

  case class OrderStatus(status: String)

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()

    //Binding
    Http().bindAndHandle(route, "127.0.0.1", 8080)
    println(s"Server online at http://127.0.0.1:8080/")

    StdIn.readLine("Hit ENTER to exit")
    system.terminate()
  }

  val foodAvailable = List("Ugali", "Rice", "beefStew", "beefFry", "Egusi", "PepperSoup")

  //Defining routes
  def route(implicit materiliazer: Materializer) = {

    pathSingleSlash {
      post {
        entity(as[OrderDesc]) { order =>
          if (foodAvailable.contains(order.name) && order.quantity > 0) {
            complete {
              //If the requirements are met...Process request
              processRequest(order)
              OrderStatus("Processing")
            }
          } else {
            complete(StatusCodes.BadRequest, OrderStatus("Bad Request!"))
          }
        }
      }
    }
  }

  //Process Request

  def processRequest(order: OrderDesc) = {

    implicit val system = ActorSystem("food-order")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val requestEntity = HttpEntity(MediaTypes.`application/json`, s"""{"name":"${order.name}","quantity":${order.quantity}}""")
    val httpRequest = HttpRequest(
      HttpMethods.POST,
      uri = "https://ether.at-labs.at-internal.com/order/request",
      entity = requestEntity,
      protocol = HttpProtocols.`HTTP/1.1`
    )

    Http().singleRequest(httpRequest).onComplete {
      case util.Success(value) => value.entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        system.log.debug(s"Food: ${order.name}|Quantity:${order.quantity}|Response: ${body.utf8String}")
      }
      case scala.util.Failure(exception) =>
        system.log.debug(s"Food: ${order.name}|Quantity:${order.quantity}|Error: ${exception.getMessage}")
    }

  }
}