package com.africasTalking.elmer
package worker.test

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal

import akka.stream.ActorMaterializer

import io.atlabs._

import horus.core.http.client.ATHttpClientResponse


trait ElmerTestHttpEndpointT extends WorkerTestService {

    final implicit val materializer = ActorMaterializer()

    def getStringHttpResponse(req: HttpRequest) = {
      getStringHttpResponseImpl(
        uri  = req.uri,
        data = Await.result(
          Unmarshal(req.entity).to[String],
          1.second
        ))
    }

    def getStringHttpResponseImpl(
       data: String,
       uri: Uri
     ): ATHttpClientResponse

}
