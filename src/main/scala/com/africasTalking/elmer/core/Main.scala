package com.africasTalking.elmer.core

import scala.io.StdIn
import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import io.atlabs.horus.core.config.ATBaseConfigT



object Main extends App {

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

}
