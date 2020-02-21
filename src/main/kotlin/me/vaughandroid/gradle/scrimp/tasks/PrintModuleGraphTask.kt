package me.vaughandroid.gradle.scrimp.tasks

import me.vaughandroid.gradle.scrimp.GradleWrapper
import me.vaughandroid.gradle.scrimp.Logger
import me.vaughandroid.gradle.scrimp.core.ModuleGraphAsTreeSerializer
import me.vaughandroid.gradle.scrimp.core.ModuleGraphBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class PrintModuleGraphTask : DefaultTask() {

    private val logger = Logger(project, true)

    @TaskAction
    fun action() {
        val gradleWrapper = GradleWrapper(project)

        val moduleGraph = ModuleGraphBuilder(logger).build(gradleWrapper, gradleWrapper)
        val treeString = ModuleGraphAsTreeSerializer.asTreeString(moduleGraph)

        logger.log("Modules:")
        logger.log(treeString)
    }

}

