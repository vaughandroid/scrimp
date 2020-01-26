package me.vaughandroid.gradle.scrimp.core

class ModuleGraph(
    rootProjectName: String
) {

    private val rootNode = Node(rootProjectName)
    val rootProjectName = rootNode.moduleName

    private val nodeLookup = mutableMapOf<String, Node>()

    val allModules : Set<String>
            get() = nodeLookup.keys

    val rootModules: Set<String>
        get() = nodeLookup.values
            .filter { it.incomingDependencies.isEmpty() }
            .map { it.moduleName }
            .toSet()

    init {
        addNode(rootNode)
    }

    fun addModule(moduleName: String) {
        getNodeAndAddIfMissing(moduleName)
    }

    fun addDependency(fromModuleName: String, toModuleName: String) {
        val fromNode = getNodeAndAddIfMissing(fromModuleName)
        val toNode = getNodeAndAddIfMissing(toModuleName)

        fromNode.outgoingDependencies += toNode
        toNode.incomingDependencies += fromNode
    }

    fun getIncomingDependencies(moduleName: String): Set<String> =
        getNodeOrError(moduleName).incomingDependencies
            .map { it.moduleName }
            .toSet()

    fun getOutgoingDependencies(moduleName: String): Set<String> =
        getNodeOrError(moduleName).outgoingDependencies
            .map { it.moduleName }
            .toSet()

    private fun getNodeAndAddIfMissing(moduleName: String): Node {
        val node = nodeLookup[moduleName] ?: Node(moduleName)
        addNode(node)
        return node
    }

    private fun getNodeOrError(moduleName: String): Node =
        nodeLookup[moduleName]
            ?: throw IllegalArgumentException("Module '$moduleName' not found.")

    private fun addNode(node: Node) {
        nodeLookup[node.moduleName] = node
    }

    private class Node(
        val moduleName: String,
        val incomingDependencies: MutableSet<Node> = mutableSetOf(),
        val outgoingDependencies: MutableSet<Node> = mutableSetOf()
    )

}