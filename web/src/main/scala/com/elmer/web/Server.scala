package com.elmer
package web

import io.atlabs.horus.core.util.{ATApplicationT, AbstractApplicationDaemon}


class ApplicationDaemon extends AbstractApplicationDaemon {
  def application = new Application
}

object ServiceApplication extends App with ATApplicationT[ApplicationDaemon] {
  def createApplication = new ApplicationDaemon
}
