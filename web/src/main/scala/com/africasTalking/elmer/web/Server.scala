package com.africasTalking.elmer.web

import akka.actor.ActorSystem

import io.atlabs._

import com.africasTalking._

import atlas.core.util.AtlasCoreServiceT

import horus.core.util.{ AbstractApplicationDaemon, ATApplicationT }

class ApplicationDaemon extends AbstractApplicationDaemon {
  def application = new Application
}

object ServiceApplication extends App with ATApplicationT[ApplicationDaemon] {
  def createApplication = new ApplicationDaemon
}
