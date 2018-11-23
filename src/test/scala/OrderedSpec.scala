import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.model._
import akka.testkit.{ImplicitSender, TestKit}
import com.africasTalking.elmer.core.Orderer
import com.africasTalking.elmer.core.Orderer.{Accepted, Failure, ProductOrder, ProductResponse}
import org.scalatest.{WordSpec, WordSpecLike}

import scala.concurrent.Future

class OrderedSpec extends TestKit(ActorSystem("switch-test-service"))
  with WordSpecLike
  with ImplicitSender
  with TestHttp {

  val test_actor  =  system.actorOf(Props(new Orderer(){
    override def sendHttpRequest(request: HttpRequest): Future[ProductResponse] = Future.successful(getHttpResponse(request))
  }))

  val mockRequest = ProductOrder("Ugali",5)

  "Orderer" must {

    "receive product order request" in {
      test_actor ! mockRequest
      expectMsg(Accepted)
    }
  }

  override def getHttpResponseImpl(data: String): ProductResponse = {
   data match {

    case _  =>
      ProductResponse(
          status = StatusCode.int2StatusCode(200),
          data   =
            """
              |{"status":"Accepted"}
            """.stripMargin
        )
    }
  }

}
