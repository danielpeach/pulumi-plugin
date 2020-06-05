package com.pulumi.preconfigured

import com.netflix.spinnaker.kork.plugins.api.PluginSdks
import com.netflix.spinnaker.orca.api.preconfigured.jobs.PreconfiguredJobConfigurationProvider
import com.netflix.spinnaker.orca.api.preconfigured.jobs.PreconfiguredJobStageProperties
import okhttp3.OkHttpClient
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory


class PulumiPlugin(wrapper: PluginWrapper): Plugin(wrapper) {
    private val logger = LoggerFactory.getLogger(PulumiPlugin::class.java)

    companion object HttpClient {
        val client = OkHttpClient()
    }

    override fun start() {
        logger.info("PulumiPlugin.start()")
    }

    override fun stop() {
        logger.info("PulumiPlugin.stop()")
    }
}

@Extension
class PulumiPreConfiguredStage(val pluginSdks: PluginSdks) : PreconfiguredJobConfigurationProvider {
    override fun getJobConfigurations(): List<PreconfiguredJobStageProperties> {
        val jobProperties = pluginSdks.yamlResourceLoader().loadResource("pulumi.yaml", PreconfiguredJobStageProperties::class.java)
        return arrayListOf(jobProperties)
    }
}
