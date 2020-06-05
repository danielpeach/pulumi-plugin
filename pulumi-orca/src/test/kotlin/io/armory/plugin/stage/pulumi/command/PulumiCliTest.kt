package io.armory.plugin.stage.pulumi.command

import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import strikt.api.expectThat
import strikt.assertions.contains
import strikt.assertions.isEqualTo

class PulumiCliTest : JUnit5Minutests {

  fun tests() = rootContext<Fixture> {

    fixture {
      Fixture()
    }

    test("executing a bundled resource") {
      expectThat(subject.execBundled("version")) {
        get { exitCode }.isEqualTo(0)
        get { result }.contains("v2.3.0")
      }
    }
  }

  inner class Fixture {
    val subject = PulumiCli()
  }
}
