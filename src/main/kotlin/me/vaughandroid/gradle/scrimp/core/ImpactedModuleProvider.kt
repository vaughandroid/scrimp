package me.vaughandroid.gradle.scrimp.core

class ImpactedModuleProvider(
    private val moduleGraph: ModuleGraph
) {

    fun getModulesImpactedByChanges(vararg changedModules: String): Set<String> =
        getModulesImpactedByChanges(changedModules.toSet())

    fun getModulesImpactedByChanges(changedModules: Set<String>): Set<String> =
        if (changedModules.contains(moduleGraph.rootProjectName)) {
            moduleGraph.allModules
        } else {
            changedModules
                // Get incoming dependencies.
                .flatMap { moduleGraph.getIncomingDependencies(it) }
                // Recursively get their dependencies.
                .flatMap { getModulesImpactedByChanges(it) }
                .plus(changedModules)
                .toSet()
        }

}