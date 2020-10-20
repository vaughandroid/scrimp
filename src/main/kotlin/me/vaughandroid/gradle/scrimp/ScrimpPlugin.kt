package me.vaughandroid.gradle.scrimp

import me.vaughandroid.gradle.scrimp.tasks.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.nio.file.Paths

@Suppress("unused")
class ScrimpPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val analysisDataFilePath = Paths.get("${project.buildDir}/scrimp/module-analysis.json")
        val taskListFilePath = Paths.get("${project.buildDir}/scrimp/filtered-tasks.txt")
        val argumentsFilePath = Paths.get("${project.buildDir}/scrimp/filtered-arguments.txt")

        val printLogs = project.properties["scrimpPrintLogs"] == "true"
        val logger = Logger(project, printLogs)

        val analyseChangedModulesTask =
            project.tasks.create(
                "scrimpAnalyse",
                AnalyseImpactedModulesTask::class.java
            ).apply {
                this.analysisDataFilePath = analysisDataFilePath
                this.logger = logger
            }

        val listTasksForImpactedModulesTask =
            project.tasks.create(
                "scrimpListTasks",
                ListTasksForImpactedModulesTask::class.java
            ).apply {
                dependsOn(analyseChangedModulesTask)
                this.analysisDataFilePath = analysisDataFilePath
                this.taskListFilePath = taskListFilePath
                this.logger = logger
            }

        project.tasks.create(
            "scrimpCreateArgumentsFile",
            CreateArgumentsFileForImpactedModulesTask::class.java
        ).apply {
            dependsOn(listTasksForImpactedModulesTask)
            this.taskListFilePath = taskListFilePath
            this.argumentsFilePath = argumentsFilePath
            this.logger = logger
        }

        project.tasks.create(
            "scrimpRun",
            RunTasksForImpactedModulesTask::class.java
        ).apply {
            dependsOn(listTasksForImpactedModulesTask)
            this.taskListFilePath = taskListFilePath
            this.logger = logger
        }

        project.tasks.create("scrimpPrintModuleGraph", PrintModuleGraphTask::class.java)
    }

}