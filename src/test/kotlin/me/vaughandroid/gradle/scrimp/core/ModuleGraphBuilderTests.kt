package me.vaughandroid.gradle.scrimp.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ModuleGraphBuilderTests {

    @Test
    fun `the root project is the root project of the module graph`() {
        // Given
        val moduleNameProvider = object : ModuleNameProvider {
            override val rootProjectName: String
                get() = "rootProject"

            override val moduleNames: Set<String>
                get() = setOf("rootProject")
        }
        val outgoingDependencyProvider = object : OutgoingDependencyProvider {
            override fun getOutgoingDependenciesForProject(projectName: String): Set<String> =
                emptySet()
        }

        // When
        val moduleGraph = ModuleGraphBuilder().build(moduleNameProvider, outgoingDependencyProvider)

        // Then
        assertThat(moduleGraph.rootProjectName).isEqualTo("rootProject")
    }

    @Test
    fun `the root project's dependencies are added to the module graph`() {
        // Given
        val moduleNameProvider = object : ModuleNameProvider {
            override val rootProjectName: String
                get() = "rootProject"

            override val moduleNames: Set<String>
                get() = setOf("rootProject", "dependencyA", "dependencyB")
        }
        val outgoingDependencyProvider = object : OutgoingDependencyProvider {
            override fun getOutgoingDependenciesForProject(projectName: String): Set<String> =
                if (projectName == "rootProject") setOf(
                    "dependencyA",
                    "dependencyB"
                ) else emptySet()
        }

        // When
        val moduleGraph = ModuleGraphBuilder().build(moduleNameProvider, outgoingDependencyProvider)

        // Then
        assertThat(moduleGraph.getOutgoingDependencies("rootProject"))
            .containsExactly("dependencyA", "dependencyB")
    }

    @Test
    fun `modules which are not direct dependencies of the root project are added to the module graph`() {
        // Given
        val moduleNameProvider = object : ModuleNameProvider {
            override val rootProjectName: String
                get() = "rootProject"

            override val moduleNames: Set<String>
                get() = setOf("rootProject", "moduleA", "moduleB")
        }
        val outgoingDependencyProvider = object : OutgoingDependencyProvider {
            override fun getOutgoingDependenciesForProject(projectName: String): Set<String> =
                emptySet()
        }

        // When
        val moduleGraph = ModuleGraphBuilder().build(moduleNameProvider, outgoingDependencyProvider)

        // Then
        assertThat(moduleGraph.rootModules)
            .containsExactly("rootProject", "moduleA", "moduleB")
    }

    @Test
    fun `module dependencies are added to the module graph`() {
        // Given
        val moduleNameProvider = object : ModuleNameProvider {
            override val rootProjectName: String
                get() = "rootProject"

            override val moduleNames: Set<String>
                get() = setOf(
                    "rootProject",
                    "dependencyA",
                    "rootModule",
                    "dependencyB",
                    "dependencyC"
                )
        }
        val outgoingDependencyProvider = object : OutgoingDependencyProvider {
            override fun getOutgoingDependenciesForProject(projectName: String): Set<String> =
                when (projectName) {
                    "rootProject" -> setOf("dependencyA")
                    "rootModule" -> setOf("dependencyB", "dependencyC")
                    else -> emptySet()
                }
        }

        // When
        val moduleGraph = ModuleGraphBuilder().build(moduleNameProvider, outgoingDependencyProvider)

        // Then
        assertThat(moduleGraph.rootModules).containsExactly("rootProject", "rootModule")
        assertThat(moduleGraph.getOutgoingDependencies("rootProject"))
            .containsExactly("dependencyA")
        assertThat(moduleGraph.getOutgoingDependencies("rootModule"))
            .containsExactly("dependencyB", "dependencyC")
    }

    /**
     *  Since modules can contain different configurations, it is possible to have circular
     *  references between modules.
     *  E.g. Module A (test configuration) -> Module B -> Module A (main configuration)
     */
    @Test
    fun `circular module dependencies are supported`() {
        // Given
        val moduleNameProvider = object : ModuleNameProvider {
            override val rootProjectName: String
                get() = "rootProject"

            override val moduleNames: Set<String>
                get() = setOf(
                    "rootProject",
                    "moduleA",
                    "moduleB"
                )
        }
        val outgoingDependencyProvider = object : OutgoingDependencyProvider {
            override fun getOutgoingDependenciesForProject(projectName: String): Set<String> =
                when (projectName) {
                    "rootProject" -> setOf("moduleA")
                    "moduleA" -> setOf("moduleB")
                    "moduleB" -> setOf("moduleA")
                    else -> emptySet()
                }
        }

        // When
        val moduleGraph = ModuleGraphBuilder().build(moduleNameProvider, outgoingDependencyProvider)

        // Then
        assertThat(moduleGraph.getOutgoingDependencies("rootProject"))
            .containsExactly("moduleA")
        assertThat(moduleGraph.getOutgoingDependencies("moduleA"))
            .containsExactly("moduleB")
        assertThat(moduleGraph.getOutgoingDependencies("moduleB"))
            .containsExactly("moduleA")
    }
}

