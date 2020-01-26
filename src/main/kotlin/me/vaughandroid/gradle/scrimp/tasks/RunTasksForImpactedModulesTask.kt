package me.vaughandroid.gradle.scrimp.tasks

import me.vaughandroid.gradle.scrimp.Logger
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.tooling.GradleConnector
import java.nio.file.Path

private const val ENABLE_DEBUG_LOGGING = true

open class RunTasksForImpactedModulesTask : DefaultTask() {

    init {
        outputs.upToDateWhen { false }
    }

    @InputFile
    lateinit var taskListFilePath: Path

    private val logger = Logger(ENABLE_DEBUG_LOGGING)

    @TaskAction
    fun action() {
        logger.log("Reading task list file: $taskListFilePath")
        val tasksToInvoke = taskListFilePath.toFile().readLines()

        logger.log("Executing tasks...")
        val connection =
            GradleConnector.newConnector().forProjectDirectory(project.projectDir).connect()
        connection.use {
            val buildLauncher = it.newBuild().forTasks(*tasksToInvoke.toTypedArray())
            buildLauncher.setStandardOutput(System.out)
            buildLauncher.setStandardError(System.err)
            buildLauncher.run()
        }
    }

}