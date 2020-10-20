package me.vaughandroid.gradle.scrimp.tasks

import me.vaughandroid.gradle.scrimp.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.tooling.GradleConnector
import java.nio.file.Path

open class RunTasksForImpactedModulesTask : DefaultTask() {

    init {
        outputs.upToDateWhen { false }
    }

    @InputFile
    lateinit var taskListFilePath: Path

    @Input
    val argumentsString: String = project.properties["scrimpExtraArgs"]?.toString() ?: ""

    lateinit var logger: Logger

    @TaskAction
    fun action() {
        logger.log("Reading task list file: $taskListFilePath")
        val tasksToInvoke = taskListFilePath.toFile().readLines()

        logger.log("Arguments: $argumentsString")

        logger.log("Executing tasks...")
        val connection =
            GradleConnector.newConnector().forProjectDirectory(project.projectDir).connect()
        connection.use {
            val buildLauncher = it.newBuild()
            buildLauncher.forTasks(*tasksToInvoke.toTypedArray())
            buildLauncher.withArguments(argumentsString)
            buildLauncher.setStandardOutput(System.out)
            buildLauncher.setStandardError(System.err)
            buildLauncher.run()
        }
    }

}