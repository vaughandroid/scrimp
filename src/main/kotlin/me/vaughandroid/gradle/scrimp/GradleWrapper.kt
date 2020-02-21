package me.vaughandroid.gradle.scrimp

import me.vaughandroid.gradle.scrimp.core.ModuleNameProvider
import me.vaughandroid.gradle.scrimp.core.OutgoingDependencyProvider
import me.vaughandroid.gradle.scrimp.core.ProjectPathProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.ProjectDependency
import java.nio.file.Path

class GradleWrapper(project: Project) : ModuleNameProvider,
    OutgoingDependencyProvider,
    ProjectPathProvider {

    private val rootProject = project.rootProject

    override val rootProjectName: String
        get() = rootProject.path

    override val moduleNames: Set<String>
        get() = rootProject.allprojects.map { it.path }.toSet()

    override fun getOutgoingDependenciesForProject(projectName: String): Set<String> {
        val project = getProjectByName(projectName)
        return project.configurations
            .flatMap { it.dependencies.withType(ProjectDependency::class.java) }
            .map { it.dependencyProject.path }
            .toSet()
    }

    override fun getPathForProject(projectName: String): Path =
        getProjectByName(projectName).projectDir.toPath()

    private fun getProjectByName(projectName: String): Project =
        rootProject.allprojects.first { it.path == projectName }

}