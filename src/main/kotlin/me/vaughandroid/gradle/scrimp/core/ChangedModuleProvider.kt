package me.vaughandroid.gradle.scrimp.core

import java.nio.file.Path

class ChangedModuleProvider(
    private val projectPathProvider: ProjectPathProvider,
    private val moduleGraph: ModuleGraph
) {

    private val modulePaths: Set<Path>
    private val pathToModuleLookup: Map<Path, String>
    private val pathMatcher: PathMatcher

    init {
        val paths = mutableSetOf<Path>()
        val lookup = mutableMapOf<Path, String>()
        moduleGraph.allModules
            .forEach {
                val path = projectPathProvider.getPathForProject(it)
                paths.add(path)
                lookup[path] = it
            }
        modulePaths = paths.toSet()
        pathToModuleLookup = lookup.toMap()
        pathMatcher = PathMatcher(modulePaths)
    }

    fun getModulesWithChanges(changedFilePaths: Set<Path>): Set<String> =
        changedFilePaths.map { findModuleForPath(it) }.toSet()

    private fun findModuleForPath(path: Path): String {
        val modulePath = pathMatcher.findClosestParent(path)
        val module = pathToModuleLookup[modulePath]
        return module ?: moduleGraph.rootProjectName
    }

}