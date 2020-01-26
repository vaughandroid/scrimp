package me.vaughandroid.gradle.scrimp.core

import java.nio.file.Path

interface ProjectPathProvider {
    fun getPathForProject(projectName: String): Path
}