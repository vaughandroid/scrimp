package me.vaughandroid.gradle.scrimp.tasks

import me.vaughandroid.gradle.scrimp.GradleWrapper
import me.vaughandroid.gradle.scrimp.core.ModuleGraphAsTreeSerializer
import me.vaughandroid.gradle.scrimp.core.ModuleGraphBuilder
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class PrintModuleGraphTask : DefaultTask() {

    @TaskAction
    fun action() {
        val gradleWrapper = GradleWrapper(project)

        val moduleGraph = ModuleGraphBuilder.build(gradleWrapper, gradleWrapper)
        val treeString = ModuleGraphAsTreeSerializer.asTreeString(moduleGraph)

        println("Modules:")
        println(treeString)
    }

}

