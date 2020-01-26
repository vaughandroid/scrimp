package me.vaughandroid.gradle.scrimp.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ModuleGraphAsTreeSerializerTests {

    @Test
    fun `the project root is listed first`() {
        // Given
        val moduleGraph = ModuleGraph("root")

        // When
        val asString = ModuleGraphAsTreeSerializer.asTreeString(moduleGraph)

        // Then
        assertThat(asString).isEqualTo("root")
    }

    @Test
    fun `all the root modules are listed alphabetically after the root project`() {
        // Given
        val moduleGraph = ModuleGraph("root").apply {
            addModule("moduleB")
            addModule("moduleA")
        }

        // When
        val asString = ModuleGraphAsTreeSerializer.asTreeString(moduleGraph)

        // Then
        val expectedString =
            """root
                |
                |moduleA
                |
                |moduleB
            """.trimMargin()
        assertThat(asString).isEqualTo(expectedString)
    }

    @Test
    fun `a module's dependencies are listed beneath it and indented`() {
        // Given
        val moduleGraph = ModuleGraph("root").apply {
            addDependency("root", "submoduleA")
            addDependency("root", "submoduleB")
        }

        // When
        val asString = ModuleGraphAsTreeSerializer.asTreeString(moduleGraph)

        // Then
        val expectedString =
            """root
                |  - submoduleA
                |  - submoduleB
            """.trimMargin()
        assertThat(asString).isEqualTo(expectedString)
    }

    @Test
    fun `sub-submodules are indented further than the submodule`() {
        // Given
        val moduleGraph = ModuleGraph("root").apply {
            addDependency("root", "submoduleA")
            addDependency("submoduleA", "submoduleB")
            addDependency("submoduleB", "submoduleC")
        }

        // When
        val asString = ModuleGraphAsTreeSerializer.asTreeString(moduleGraph)

        // Then
        val expectedString =
            """root
                |  - submoduleA
                |      - submoduleB
                |          - submoduleC
            """.trimMargin()
        assertThat(asString).isEqualTo(expectedString)
    }

    @Test
    fun `a dependency's dependencies are listed before the next dependency`() {
        // Given
        val moduleGraph = ModuleGraph("root").apply {
            addDependency("root", "submoduleA")
            addDependency("root", "submoduleB")
            addDependency("submoduleA", "submoduleC")
            addDependency("submoduleB", "submoduleD")
        }

        // When
        val asString = ModuleGraphAsTreeSerializer.asTreeString(moduleGraph)

        // Then
        val expectedString =
            """root
                |  - submoduleA
                |      - submoduleC
                |  - submoduleB
                |      - submoduleD
            """.trimMargin()
        assertThat(asString).isEqualTo(expectedString)
    }

    @Test
    fun `modules with multiple incoming dependencies appear more than once`() {
        // Given
        val moduleGraph = ModuleGraph("root").apply {
            addDependency("root", "submoduleA")
            addDependency("root", "submoduleB")
            addDependency("submoduleA", "submoduleB")
        }

        // When
        val asString = ModuleGraphAsTreeSerializer.asTreeString(moduleGraph)

        // Then
        val expectedString =
            """root
                |  - submoduleA
                |      - submoduleB
                |  - submoduleB
            """.trimMargin()
        assertThat(asString).isEqualTo(expectedString)
    }

}

