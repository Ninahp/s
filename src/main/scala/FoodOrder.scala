import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.ByteString

import scala.io.StdIn


object FoodOrder {

  case class Order(name: String, quantity: Int)

  case class OrderStatus(status: String)

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()


    Http().bindAndHandle(route, "127.0.0.1", 8000)

    StdIn.readLine("Hit ENTER to exit")
    system.terminate()
  }

  val meals = List("Ugali", "Rice", "beefStew", "beefFry", "Egusi", "PepperSoup")

  def route(implicit mat: Materializer) = {
    import Directives._
    import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
    import io.circe.generic.auto._

    pathSingleSlash {
      post {
        entity(as[Order]) { order =>
          if (meals.contains(order.name) && order.quantity > 0) {
            complete {
              processBrokerRequest(order)
              OrderStatus("Processing")
            }
          } else {
            complete(StatusCodes.BadRequest, OrderStatus("Invalid Request"))
          }
        }
      }
    }
  }

  def processBrokerRequest(order: Order) = {

    implicit val system = ActorSystem("food-order")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val requestEntity = HttpEntity(MediaTypes.`application/json`,s"""{"name":"${order.name}","quantity":${order.quantity}}""")
    val httpRequest = HttpRequest(HttpMethods.POST,
      uri = "https://ether.at-labs.at-internal.com/order/request",
      entity = requestEntity,
      protocol = HttpProtocols.`HTTP/1.1`
    )

    Http().singleRequest(httpRequest).onComplete {
      case util.Success(value) => value.entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        system.log.debug(s"Meal: ${order.name}|Quantity:${order.quantity}|Response: ${body.utf8String}")
      }
      case scala.util.Failure(exception) =>
        system.log.debug(s"Meal: ${order.name}|Quantity:${order.quantity}|Error: ${exception.getMessage}")
    }

  }
}