package me.vaughandroid.gradle.scrimp.core

object ModuleGraphAsTreeSerializer {

    fun asTreeString(moduleGraph: ModuleGraph): String {
        val sb = StringBuilder()

        // Root project first.
        val rootProjectName = moduleGraph.rootProjectName
        sb.appendModule(rootProjectName, 0, moduleGraph, emptySet())
        sb.appendLine()

        // Root modules, alphabetically.
        moduleGraph.rootModules
            .filter { it != rootProjectName }
            .sorted()
            .forEach {
                sb.appendModule(it, 0, moduleGraph, emptySet())
                sb.appendLine()
            }

        return sb.trimEnd().toString()
    }

    private fun StringBuilder.appendModule(
        moduleName: String,
        level: Int = 0,
        moduleGraph: ModuleGraph,
        ancestors: Set<String>
    ) {
        appendLine(moduleName)
        moduleGraph.getOutgoingDependencies(moduleName).forEach {
            repeat(level) { append("    ") }
            append("  - ")
            if (ancestors.contains(it)) {
                append(it).appendLine(" (*)")
            } else {
                appendModule(it, level + 1, moduleGraph, ancestors.plus(it))
            }
        }
    }

}