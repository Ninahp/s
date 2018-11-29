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
import elmer.worker.gateway.GatewayJsonSupportT
import elmer.worker.gateway._
import elmer.worker.test.ElmerTestHttpEndpointT


class FoodRequestGatewaySpec extends ElmerTestHttpEndpointT with GatewayJsonSupportT {

  val gatewayService   = system.actorOf(Props(new FoodRequestGateway{
    override def sendHttpRequest(req : HttpRequest) =
      Future.successful(getStringHttpResponse(req))
  }))


  "Food Request Gateway" must {
    "Send and receive a response from the broker when sent a valid Request" in {
      gatewayService ! FoodGatewayRequest(
        FoodEnum.Ugali,
        12
      )
      expectMsg(FoodGatewayResponse(FoodOrderStatus.Accepted, "Request Accepted"))
    }

    "Send and receive an Invalid response from the broker when sent an invalid Request" in {
      gatewayService ! FoodGatewayRequest(
        FoodEnum.Invalid,
        12
      )
      expectMsg(FoodGatewayResponse(FoodOrderStatus.BadRequest, "Error while sending request to the gateway"))
    }

    "Send and receive a BadRequest from the broker when sent rice " in {
      gatewayService ! FoodGatewayRequest(
        FoodEnum.Rice,
        12
      )
      expectMsg(FoodGatewayResponse(FoodOrderStatus.BadRequest, "Content was malformed"))
    }

  }

  override def getStringHttpResponseImpl(
     data: String,
     uri:Uri
   ): ATHttpClientResponse = {
          val request = data.parseJson.convertTo[etherGatewayRequest]
          request.name match{
            case FoodEnum.Ugali =>
              ATHttpClientResponse(
                status = StatusCodes.OK,
                data   = etherGatewayResponse(
                  status = FoodOrderStatus.Accepted
                ).toJson.compactPrint
              )

            case FoodEnum.Rice  =>
              ATHttpClientResponse(
                status = StatusCodes.OK,
                data   = etherGatewayResponse(
                  status = FoodOrderStatus.BadRequest
                ).toJson.compactPrint
              )

            case FoodEnum.Invalid =>
              ATHttpClientResponse(
                status = StatusCodes.BadRequest,
                data   = etherGatewayResponse(
                  status = FoodOrderStatus.InternalError
                ).toJson.compactPrint
              )

            case _ =>
              ATHttpClientResponse(
                status = StatusCodes.Forbidden,
                data   = etherGatewayResponse(
                  status = FoodOrderStatus.InternalError
                ).toJson.compactPrint
              )

          }
  }
}