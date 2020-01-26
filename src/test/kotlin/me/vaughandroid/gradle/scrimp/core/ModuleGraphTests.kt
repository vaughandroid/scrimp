package me.vaughandroid.gradle.scrimp.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ModuleGraphTests {

    @Test
    fun `the root project is the root of the graph`() {
        // When
        val moduleGraph = ModuleGraph("root")

        // Then
        assertThat(moduleGraph.rootProjectName).isEqualTo("root")
    }

    @Test
    fun `a module can be added`() {
        // Given
        val moduleGraph = ModuleGraph("root")

        // When
        moduleGraph.addModule("submodule")

        // Then
        assertThat(moduleGraph.allModules).contains("submodule")
    }

    @Test
    fun `a dependency of the root project can be added`() {
        // Given
        val moduleGraph = ModuleGraph("root")

        // When
        moduleGraph.addDependency("root", "dependency")

        // Then
        assertThat(moduleGraph.getOutgoingDependencies("root")).contains("dependency")
        assertThat(moduleGraph.getIncomingDependencies("dependency")).contains("root")
    }

    @Test
    fun `a dependency can be created for a module which has been added`() {
        // Given
        val moduleGraph = ModuleGraph("root").apply {
            addModule("module")
        }

        // When
        moduleGraph.addDependency("module", "dependency")

        // Then
        assertThat(moduleGraph.getOutgoingDependencies("module")).contains("dependency")
        assertThat(moduleGraph.getIncomingDependencies("dependency")).contains("module")
    }

    @Test
    fun `a dependency can be created for a module which has not been added`() {
        // Given
        val moduleGraph = ModuleGraph("root")

        // When
        moduleGraph.addDependency("module", "dependency")

        // Then
        assertThat(moduleGraph.getOutgoingDependencies("module")).contains("dependency")
        assertThat(moduleGraph.getIncomingDependencies("dependency")).contains("module")
    }

    @Test
    fun `modules can have more than one dependency`() {
        // Given
        val moduleGraph = ModuleGraph("root")

        // When
        moduleGraph.addDependency("root", "submoduleA")
        moduleGraph.addDependency("root", "submoduleB")
        moduleGraph.addDependency("root", "submoduleC")

        // Then
        assertThat(moduleGraph.getOutgoingDependencies("root"))
            .containsAtLeast("submoduleA", "submoduleB", "submoduleC")
        assertThat(moduleGraph.getIncomingDependencies("submoduleA")).contains("root")
        assertThat(moduleGraph.getIncomingDependencies("submoduleB")).contains("root")
        assertThat(moduleGraph.getIncomingDependencies("submoduleC")).contains("root")
    }

    @Test
    fun `the root project is listed as a root module`() {
        // Given
        val moduleGraph = ModuleGraph("root")

        // When
        val rootModules = moduleGraph.rootModules

        // Then
        assertThat(rootModules).contains("root")
    }

    @Test
    fun `modules with no incoming dependencies are listed as root modules`() {
        // Given
        val moduleGraph = ModuleGraph("root").apply {
            addModule("rootModuleWithoutDependencies")
            addDependency("rootModuleWithDependencies", "dependency")
        }

        // When
        val rootModules = moduleGraph.rootModules

        // Then
        assertThat(rootModules).contains("root")
        assertThat(rootModules).contains("rootModuleWithoutDependencies")
        assertThat(rootModules).contains("rootModuleWithDependencies")
    }

    @Test
    fun `modules with incoming dependencies are not listed as root modules`() {
        // Given
        val moduleGraph = ModuleGraph("root").apply {
            addModule("dependencyA")
            addDependency("root", "dependencyA")
            addDependency("root", "dependencyB")
        }

        // When
        val rootModules = moduleGraph.rootModules

        // Then
        assertThat(rootModules).doesNotContain("dependencyA")
        assertThat(rootModules).doesNotContain("dependencyB")
    }

    @Test
    fun `adding a module which has already been added does not affect its dependencies`() {
        // Given
        val moduleGraph = ModuleGraph("root").apply {
            addDependency("root", "module")
            addDependency("module", "submodule")
        }

        // When
        moduleGraph.addModule("module")

        // Then
        assertThat(moduleGraph.getIncomingDependencies("module"))
            .containsExactly("root")
        assertThat(moduleGraph.getOutgoingDependencies("module"))
            .containsExactly("submodule")
    }

    @Test
    fun `adding a dependency on an existing module does not affect its existing dependencies`() {
        // Given
        val moduleGraph = ModuleGraph("root").apply {
            addDependency("root", "module")
            addDependency("module", "submoduleA")
        }

        // When
        moduleGraph.addDependency("module", "submoduleB")

        // Then
        assertThat(moduleGraph.getIncomingDependencies("module"))
            .containsExactly("root")
        assertThat(moduleGraph.getOutgoingDependencies("module"))
            .containsExactly("submoduleA", "submoduleB")
    }

}

