package me.vaughandroid.gradle.scrimp.core

object ModuleGraphBuilder {

    fun build(
        moduleNameProvider: ModuleNameProvider,
        outgoingDependencyProvider: OutgoingDependencyProvider
    ): ModuleGraph {
        val rootProjectName = moduleNameProvider.rootProjectName
        val moduleGraph = ModuleGraph(rootProjectName)
        moduleNameProvider.moduleNames.forEach { moduleName ->
            moduleGraph.addModuleAndDependencies(moduleName, outgoingDependencyProvider)
        }
        return moduleGraph
    }

    private fun ModuleGraph.addModuleAndDependencies(
        moduleName: String,
        outgoingDependencyProvider: OutgoingDependencyProvider
    ) {
        addModule(moduleName)
        outgoingDependencyProvider.getOutgoingDependenciesForProject(moduleName)
            .forEach { dependencyModuleName ->
                addDependency(moduleName, dependencyModuleName)
                addModuleAndDependencies(dependencyModuleName, outgoingDependencyProvider)
            }
    }
}