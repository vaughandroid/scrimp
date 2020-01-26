package me.vaughandroid.gradle.scrimp.core

object ModuleGraphAsTreeSerializer {

    fun asTreeString(moduleGraph: ModuleGraph): String {
        val sb = StringBuilder()

        // Root project first.
        val rootProjectName = moduleGraph.rootProjectName
        sb.appendModule(rootProjectName, 0, moduleGraph)
        sb.appendln()

        // Root modules, alphabetically.
        moduleGraph.rootModules
            .filter { it != rootProjectName }
            .sorted()
            .forEach {
                sb.appendModule(it, 0, moduleGraph)
                sb.appendln()
            }

        return sb.trimEnd().toString()
    }

    private fun StringBuilder.appendModule(
        moduleName: String,
        level: Int = 0,
        moduleGraph: ModuleGraph
    ) {
        appendln(moduleName)
        moduleGraph.getOutgoingDependencies(moduleName).forEach {
            repeat(level) { append("    ") }
            append("  - ").appendModule(it, level + 1, moduleGraph)
        }
    }

}