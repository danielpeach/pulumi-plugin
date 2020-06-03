package io.armory.plugin.stage.pulumi.command

import io.armory.plugin.stage.pulumi.PulumiPlugin
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.io.File
import java.util.concurrent.TimeUnit

class PulumiCli {

    private val logger = LoggerFactory.getLogger(PulumiCli::class.java)
    private var credentials = emptyMap<String, String?>()
    private val commandTimeout = 30L
    private var path = "/home/spinnaker"
    private val installPath = "$path/.pulumi/bin"

    fun getLatestVersion(): String {
        val request = Request.Builder()
                .url("https://www.pulumi.com/latest-version")
                .get()
                .build()

        PulumiPlugin.client.newCall(request).execute().use { response ->
            if (!response.isSuccessful()) {
                throw Exception("Could not fetch latest version info from Pulumi.")
            }

            return response.body()!!.string()
        }
    }

    fun install(version: String?): CommandResponse {
        var installVersion = ""
        if (version.isNullOrEmpty() || version.equals("latest")) {
            try {
                installVersion = getLatestVersion()
            } catch(ex: Exception) {
                CommandResponse(1, ex.message!!)
            }
        } else {
            installVersion = version
        }

        val outputFile = "$path/pulumi-v$installVersion.tar.gz"
        var resp = exec(listOf("wget", "-O", outputFile, "https://get.pulumi.com/releases/sdk/pulumi-v$installVersion-linux-x64.tar.gz"))
        if (resp.exitCode!! > 0) {
            return resp
        }

        resp = exec(listOf("tar", "zxf", outputFile))
        if (resp.exitCode!! > 0) {
            return resp
        }

        resp = exec(listOf("mkdir", "-p", installPath))
        if (resp.exitCode!! > 0) {
            return resp
        }

        resp = exec(listOf("mv", "$path/pulumi/*", installPath))
        return resp
    }

    fun selectStack(stack: String): CommandResponse {
        return exec(listOf("pulumi","stack","select", stack))
    }

    fun up(): CommandResponse {
        return exec(listOf("pulumi","up","--yes", "--non-interactive"))
    }

    fun build(language: String): CommandResponse {
        return when(language){
            "Typescript" -> exec(listOf("npm", "install"))
            else -> CommandResponse(null, "")
        }
    }

    fun setCredentials(credentials: Map<String, String?>){
        this.credentials = credentials
    }

    fun setPath(path: String){
        this.path = path
    }

    private fun exec(command: List<String>): CommandResponse {
        val process = ProcessBuilder(command)
        process.environment().putAll(credentials)
        process.directory(File(path))
        val resultProcess = process.start()
        val result: StringBuilder = StringBuilder()
        resultProcess.inputStream.reader(Charsets.UTF_8).use {
            val content = it.readText()
            print(content)
            result.append(content)
        }
        resultProcess.waitFor(commandTimeout, TimeUnit.SECONDS)

        return CommandResponse(resultProcess.exitValue(), result.toString())
    }

}