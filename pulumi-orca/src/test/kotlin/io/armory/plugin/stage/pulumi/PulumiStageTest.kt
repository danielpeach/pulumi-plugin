package io.armory.plugin.stage.pulumi

import com.netflix.spinnaker.orca.api.simplestage.SimpleStageInput
import com.netflix.spinnaker.orca.api.simplestage.SimpleStageStatus
import dev.minutest.junit.JUnit5Minutests
import dev.minutest.rootContext
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class PulumiStageTest : JUnit5Minutests {

    fun tests() = rootContext {

        test("pulumi should throw an error when AWS credentials are not set"){

            // given
            val config = PulumiConfig(version = "latest")
            val input = PulumiInput("aws", "https://github.com/praneetloke/pulumi-simple-website", "master", "dev", credentials = null, version = "latest", accessToken = "pul-token")

            // when
            val result = PulumiStage(config).execute(SimpleStageInput(input))

            // then
            expectThat(result.status).isEqualTo(SimpleStageStatus.TERMINAL)
        }
    }
}
