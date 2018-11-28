package com.africasTalking.elmer.worker


import spray.json.DefaultJsonProtocol
import com.africasTalking._
import elmer.core.util.ElmerEnum.Food
import OrderService.{OrderServiceRequest, OrderServiceResponse}



trait ProductMarshaller extends DefaultJsonProtocol{


  implicit val order = jsonFormat2(OrderServiceRequest)
  implicit val response = jsonFormat1(OrderServiceResponse)

}
