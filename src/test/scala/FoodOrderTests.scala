
import FoodOrder.Order
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit._
import org.scalatest.{Matchers, WordSpec}


class FoodOrderTests extends WordSpec with Matchers with ScalatestRouteTest {
  lazy val routes: Route = FoodOrder.route
  "be able to place order (POST /)" in {
    val order = Order("Ugali", 1)
    val body =s"""{"name":"${order.name}","quantity":${order.quantity}}"""
    val orderEntity = HttpEntity(MediaTypes.`application/json`, body)

    // using the RequestBuilding DSL:
    val request = Post("/").withEntity(orderEntity)

    request ~> routes ~> check {
      status should ===(StatusCodes.OK)

      // we expect the response to be json:
      contentType should ===(ContentTypes.`application/json`)

      // and we know what message we're expecting back:
      entityAs[String] should ===("""{"status":"Processing"}""")
    }
  }

}

