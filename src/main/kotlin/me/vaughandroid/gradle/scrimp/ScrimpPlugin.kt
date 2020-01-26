package me.vaughandroid.gradle.scrimp

import me.vaughandroid.gradle.scrimp.tasks.AnalyseImpactedModulesTask
import me.vaughandroid.gradle.scrimp.tasks.FilterTasksForImpactedModulesTask
import me.vaughandroid.gradle.scrimp.tasks.PrintModuleGraphTask
import me.vaughandroid.gradle.scrimp.tasks.RunTasksForImpactedModulesTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.nio.file.Paths

@Suppress("unused")
class ScrimpPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val analysisDataFilePath = Paths.get("${project.buildDir}/scrimp/module-analysis.json")
        val taskListFilePath = Paths.get("${project.buildDir}/scrimp/filtered-tasks.txt")

        val analyseChangedModulesTask =
            project.tasks.create(
                "scrimpAnalyse",
                AnalyseImpactedModulesTask::class.java
            ).apply {

                this.analysisDataFilePath = analysisDataFilePath
            }

        val filterTasksForImpactedModulesTask =
            project.tasks.create(
                "scrimpFilter",
                FilterTasksForImpactedModulesTask::class.java
            ).apply {
                dependsOn(analyseChangedModulesTask)
                this.analysisDataFilePath = analysisDataFilePath
                this.taskListFilePath = taskListFilePath
            }

        project.tasks.create(
            "scrimpRun",
            RunTasksForImpactedModulesTask::class.java
        )
            .apply {
                dependsOn(filterTasksForImpactedModulesTask)
                this.taskListFilePath = taskListFilePath
            }

        project.tasks.create("scrimpPrintModuleGraph", PrintModuleGraphTask::class.java)
    }

}