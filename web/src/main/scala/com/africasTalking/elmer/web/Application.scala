package com.africasTalking.elmer
package web

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http

import io.atlabs._

import horus.core.util.{ ATLogT, ApplicationLifecycle }

import core.ElmerConfig

class Application extends ApplicationLifecycle with ATLogT {

  private[this] var isStarted =  false
  private val appName = "Elmer"
    implicit val system = ActorSystem(s"$appName")
    implicit val context = system.dispatcher
    implicit val materializer = ActorMaterializer()


   def start(): Unit = {

     log.info(s"Starting $appName service")
     if(!isStarted){
       Http().bindAndHandle(new RestApi().routes,
         ElmerConfig.host,
         ElmerConfig.port)
     }
     isStarted = true

   }

  override def stop(): Unit = {
    log.info(s"Stopping $appName service")
    if(isStarted){
      isStarted = false
      system.terminate()
    }
  }
}
