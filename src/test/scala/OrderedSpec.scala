//import Orderer.{Accepted, ProductOrder}
//import akka.actor.{ActorSystem, Props}
//import akka.http.scaladsl.model._
//import akka.testkit.{ImplicitSender, TestKit}
//import org.scalatest.{WordSpec, WordSpecLike}
//
//import scala.concurrent.Future
//
//class OrderedSpec extends TestKit(ActorSystem("switch-test-service"))
//  with WordSpecLike
//  with ImplicitSender
//  with TestHttp {
//
//  val test_actor  =  system.actorOf(Props(new Orderer(){
//    override def sendHttpRequest(req : HttpRequest) = Future.successful(getHttpResponse(req))
//  }))
//
//  val mockRequest = ProductOrder("Ugali",5)
//
//  "Orderer" must {
//
//    "receive produtc order request" in {
//      test_actor ! mockRequest
//      expectMsg(Accepted)
//    }
//  }
//
//  override def getHttpResponseImpl(data: String): ATHttpClientResponse = {
//   data match {
//
//    case _  =>
//      ATHttpClientResponse(
//          status = StatusCode.int2StatusCode(200),
//          data   =
//            """
//              |{"status":"Accepted"}
//            """.stripMargin
//        )
//    }
//  }
//
//}
