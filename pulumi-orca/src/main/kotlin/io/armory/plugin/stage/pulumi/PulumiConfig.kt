package io.armory.plugin.stage.pulumi

import com.netflix.spinnaker.kork.plugins.api.ExtensionConfiguration
import com.netflix.spinnaker.kork.plugins.api.PluginConfiguration
import io.armory.plugin.stage.pulumi.model.Account
import io.armory.plugin.stage.pulumi.model.Credentials

/**
 * Data in this class maps to the plugin configuration in a service's config YAML.
 * The data can be key/value pairs or an entire configuration tree.
 *
 */
@ExtensionConfiguration("armory.pulumiStage")
data class PulumiConfig(
        var version: String?
)
