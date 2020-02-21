package me.vaughandroid.gradle.scrimp.tasks

import me.vaughandroid.gradle.scrimp.Logger
import me.vaughandroid.gradle.scrimp.data.AnalysisStore
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path

open class ListTasksForImpactedModulesTask : DefaultTask() {

    @Input
    val tasksString = project.properties["scrimpTasks"]?.toString()

    @InputFile
    lateinit var analysisDataFilePath: Path

    @OutputFile
    lateinit var taskListFilePath: Path

    var printLogs = false

    private val logger = Logger(project, printLogs)

    @TaskAction
    fun action() {
        val analysisData = AnalysisStore(analysisDataFilePath).retrieve()
            ?: throw GradleException("Analysis data file not found at $analysisDataFilePath")

        val taskNames = tasksString?.split(" ")
            ?: throw GradleException("'scrimpTasks' property not set")
        logger.logList(taskNames, "Task names: ")

        val modules = analysisData.modulesImpactedByChanges
        logger.logList(modules, "Impacted modules: ")

        val tasksToInvoke = modules
            .toList()
            .sorted()
            .flatMap { moduleName ->
                taskNames
                    .map { taskName ->
                        "$moduleName:$taskName"
                    }
                    .filter { taskPath ->
                        project.tasks.findByPath(taskPath) != null
                    }

            }
        logger.logList(tasksToInvoke, "Tasks to invoke: ")

        logger.logList(tasksToInvoke, "Writing task list file: $taskListFilePath")
        val taskListText = tasksToInvoke.joinToString(separator = "\n")
        taskListFilePath.toFile().writeText(taskListText)
    }

}