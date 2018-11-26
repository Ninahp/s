package com.africasTalking.elmer.core
package config

import org.scalatest.FlatSpec

import io.atlabs._

import horus.core.util.ATEnum.ATEnvironment

class ElmerConfigSpec extends FlatSpec {
  object TestDevConfig extends ElmerConfigT {
    override protected def getEnvironmentImpl = "dev"
  }

  object TestStagingConfig extends ElmerConfigT {
    override protected def getEnvironmentImpl = "staging"
  }

  object TestSandboxConfig extends ElmerConfigT {
    override protected def getEnvironmentImpl = "sandbox"
  }

  object TestProdConfig extends ElmerConfigT {
    override protected def getEnvironmentImpl = "prod"
  }

  "The ATConfig" should "come up correctly" in {
    assert(TestDevConfig.getEnvironment == ATEnvironment.Development)
    assert(TestStagingConfig.getEnvironment == ATEnvironment.Staging)
    assert(TestSandboxConfig.getEnvironment == ATEnvironment.Sandbox)
    assert(TestProdConfig.getEnvironment == ATEnvironment.Production)
  }
}
