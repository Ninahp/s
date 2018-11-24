package com.africasTalking.elmer
package web


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import core.ElmerConfig
import io.atlabs._

import scala.io.StdIn
import scala.concurrent.duration._



object Main extends App { // ApplicationLifecycle with ATLogT


  implicit val system = ActorSystem()
  implicit val context = system.dispatcher
  implicit val materializer = ActorMaterializer()
  implicit val timeout = Timeout(5 seconds)

  val host = ElmerConfig.host
  val port = ElmerConfig.port

  val routes = new RestApi().routes

  val bindingFuture = Http().bindAndHandle(routes,interface = host,port = port)
  println(s"Server online at http://localhost:$port/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
//  private[this] var isStarted =  false
//  private val appName = "Elmer"
//    implicit val system = ActorSystem()
//    implicit val context = system.dispatcher
//    implicit val materializer = ActorMaterializer()
//
//
//   def start(): Unit = {
//
//     log.info(s"Starting $appName service")
//     if(!isStarted){
//       Http().bindAndHandle(new RestApi().routes,
//         ElmerConfig.host,
//         ElmerConfig.port)
//     }
//     isStarted = true
//
//   }
//
//  override def stop(): Unit = {
//    log.info(s"Stopping $appName service")
//    if(isStarted){
//      isStarted = false
//      system.terminate()
//    }
//  }
}
