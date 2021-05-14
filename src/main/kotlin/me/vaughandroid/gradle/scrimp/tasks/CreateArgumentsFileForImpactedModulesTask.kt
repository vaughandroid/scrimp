package me.vaughandroid.gradle.scrimp.tasks

import me.vaughandroid.gradle.scrimp.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.nio.file.Path

open class CreateArgumentsFileForImpactedModulesTask : DefaultTask() {

    init {
        outputs.upToDateWhen { false }
    }

    @InputFile
    lateinit var taskListFilePath: Path

    @Input
    val argumentsString: String = project.properties["scrimpExtraArgs"]?.toString() ?: ""

    @OutputFile
    lateinit var argumentsFilePath: Path

    @Internal
    lateinit var logger: Logger

    @TaskAction
    fun action() {
        logger.log("Reading task list file: $taskListFilePath")
        val tasksToInvoke = taskListFilePath.toFile().readLines()

        logger.log("Input arguments: $argumentsString")

        logger.log("Building arguments output")
        val buffer = StringBuffer()
        tasksToInvoke.joinTo(buffer, separator = " ")
        if (argumentsString.isNotEmpty()) {
            buffer.append(" ").append(argumentsString)
        }
        val output = buffer.toString()

        logger.log("Writing arguments file: $argumentsFilePath")
        argumentsFilePath.toFile().writeText(output)
    }

}