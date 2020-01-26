package me.vaughandroid.gradle.scrimp.core

interface OutgoingDependencyProvider {
    fun getOutgoingDependenciesForProject(projectName: String): Set<String>
}