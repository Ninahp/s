package com.africasTalking.elmer
package worker

import scala.concurrent._

import akka.actor.Props
import akka.http.scaladsl.model._

import spray.json._

import io.atlabs._

import horus.core.http.client.ATHttpClientResponse

import com.africasTalking._

import elmer.core.util.ElmerEnum._

import elmer.worker.gateway.FoodRequestGateway.{ FoodGatewayRequest, FoodGatewayResponse }
import elmer.worker.gateway.FoodRequestGateway
import elmer.worker.test.{ ElmerTestHttpEndpointT, WorkerTestService }


class FoodRequestGatewaySpec extends WorkerTestService
  with ElmerTestHttpEndpointT
  with GatewayJsonSupportT {

  val gatewayService  = system.actorOf(Props(new FoodRequestGateway{
    override def sendHttpRequest(req : HttpRequest) = Future.successful(getStringHttpResponse(req))
    override val gatewayUrl  = "http://at.foodservice.com/request"
  }))

  val validRequest    = FoodGatewayRequest(name = "PepperSoup", quantity = 12)
  val invalidRequest  = FoodGatewayRequest(name = "Sauce", quantity = 0)

  val validResponse   = FoodGatewayResponse(FoodOrderStatus.Accepted,"Request Accepted")
  val invalidResponse = FoodGatewayResponse(FoodOrderStatus.BadRequest,"Content was malformed")

  "Food Request Gateway" must {
    "Send and receive a response from the broker when sent a valid Request" in {
      gatewayService ! validRequest
      expectMsg(validResponse)
    }

    "Send and receive a failed response from the broker when sent an invalid Request" in {
      gatewayService ! invalidRequest
      expectMsg(invalidResponse)
    }
  }

  override def getStringHttpResponseImpl(
     data: String,
     uri:Uri
   ): ATHttpClientResponse = {
      uri.toString match{
        case "http://at.foodservice.com/request" =>
          val request = data.parseJson.convertTo[GatewayRequest]
          FoodEnum.contains(request.name) match{
            case false =>
              ATHttpClientResponse(
                status = StatusCodes.OK,
                data   = FoodGatewayResponse(
                  FoodOrderStatus.BadRequest,
                  "Error while sending request to the gateway"
                ).toJson.compactPrint
              )

            case true  =>
              ATHttpClientResponse(
                status = StatusCodes.OK,
                data   = FoodGatewayResponse(
                  FoodOrderStatus.Accepted,
                  "Request Accepted"
                ).toJson.compactPrint
              )
          }

        case _=>
          ATHttpClientResponse(
            status = StatusCodes.NotFound,
            data   = "The page you requested is not available"
          )
      }
  }
}