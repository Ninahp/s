package com.elmer
package web

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http

import io.atlabs.horus.core.util.

import com.elmer.core.config.FoodConfig


class Application extends ApplicationLifecycle with ATLogT {

  private var  started: Boolean       = false
  private val  applicationName        = "elmer-foods"
  implicit val actorSystem            = ActorSystem(s"$applicationName-system")

  def start()  {
    logger.info(s"Starting $applicationName Service at 8000")

    if (!started) {

      implicit val materializer = ActorMaterializer()

      Http().bindAndHandle(
        new ElmerWebServiceT {
          override def actorRefFactory = actorSystem
        }.route,
        FoodConfig.webPort,
        FoodConfig.webHost
      )

      started = true
    }
  }

  def stop() {
    logger.info(s"Stopping $applicationName Service")
    Await.result(actorSystem.whenTerminated, Duration.Inf)

    if (started) {
      started = false
      actorSystem.terminate()
    }
  }

}
