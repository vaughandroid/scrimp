package me.vaughandroid.gradle.scrimp.core

class ModuleGraphBuilder(
    private val logReceiver: LogReceiver? = null
) {

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

        val outgoingDependencies =
            outgoingDependencyProvider.getOutgoingDependenciesForProject(moduleName)

        logReceiver?.logList(outgoingDependencies, "Add module $moduleName with dependencies:")

        outgoingDependencies
            .forEach { dependencyModuleName ->
                addDependency(moduleName, dependencyModuleName)
                addModuleAndDependencies(dependencyModuleName, outgoingDependencyProvider)
            }
    }
}