package me.vaughandroid.gradle.scrimp.tasks

import me.vaughandroid.gradle.scrimp.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path

open class CreateArgumentsFileForImpactedModulesTask : DefaultTask() {

    init {
        outputs.upToDateWhen { false }
    }

    @InputFile
    lateinit var taskListFilePath: Path

    @Input
    val argumentsString = project.properties["scrimpExtraArgs"]?.toString()

    @OutputFile
    lateinit var argumentsFilePath: Path

    lateinit var logger: Logger

    @TaskAction
    fun action() {
        logger.log("Reading task list file: $taskListFilePath")
        val tasksToInvoke = taskListFilePath.toFile().readLines()

        logger.log("Input arguments: $argumentsString")

        logger.log("Writing arguments file: $argumentsFilePath")
        val taskListText = tasksToInvoke.joinTo(StringBuffer(), separator = " ")
            .append(" ")
            .append(argumentsString)
            .toString()
        argumentsFilePath.toFile().writeText(taskListText)
    }

}