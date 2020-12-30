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
            val nextGeneration = changedModules
                    .flatMap { moduleGraph.getIncomingDependencies(it) }
                    .plus(changedModules)
                    .toSet()
            if (nextGeneration.size > changedModules.size) {
                getModulesImpactedByChanges(nextGeneration)
            } else {
                changedModules
            }
        }

}