package io.armory.plugin.stage.pulumi

import io.armory.plugin.stage.pulumi.model.Credentials

/**
 * This the the part of the Context map that we care about as input to the stage execution.
 * The data can be key/value pairs or an entire configuration tree.
 */
data class PulumiInput(
        var cloudProvider: String,
        var githubRepository: String, // https://github.com/spinnaker-hackathon/pulumi-plugin
        var githubBranch: String, // master
        var stackName: String, //dev
        var version: String,
        var accessToken: String,

        var credentials: Credentials?
)