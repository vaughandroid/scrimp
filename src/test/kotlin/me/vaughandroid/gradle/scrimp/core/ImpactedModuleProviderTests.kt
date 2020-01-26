package me.vaughandroid.gradle.scrimp.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ImpactedModuleProviderTests {

    /*
    rootProject
    moduleA
      - moduleF
    moduleB
      - moduleC
      - moduleD
        - moduleE
        - moduleF
     */
    private val moduleGraph = ModuleGraph("rootProject").apply {
        addModule("moduleA")
        addModule("moduleB")
        addDependency("moduleB", "moduleC")
        addDependency("moduleB", "moduleD")
        addDependency("moduleD", "moduleE")
        addDependency("moduleD", "moduleF")
        addDependency("moduleA", "moduleF")
    }

    @Test
    fun `a change to the root project means all modules are impacted`() {
        // Given
        val impactedModuleProvider = ImpactedModuleProvider(moduleGraph)

        // When
        val impactedModules = impactedModuleProvider.getModulesImpactedByChanges("rootProject")

        // Then
        assertThat(impactedModules).containsExactly(
            "rootProject",
            "moduleA",
            "moduleB",
            "moduleC",
            "moduleD",
            "moduleE",
            "moduleF"
        )
    }

    @Test
    fun `a change to a module with no incoming dependencies returns just that module`() {
        // Given
        val impactedModuleProvider = ImpactedModuleProvider(moduleGraph)

        // When
        val impactedModules = impactedModuleProvider.getModulesImpactedByChanges("moduleA")

        // Then
        assertThat(impactedModules).containsExactly("moduleA")
    }

    @Test
    fun `changes to multiple modules without incoming dependencies returns just those modules`() {
        // Given
        val impactedModuleProvider = ImpactedModuleProvider(moduleGraph)

        // When
        val impactedModules =
            impactedModuleProvider.getModulesImpactedByChanges("moduleA", "moduleB")

        // Then
        assertThat(impactedModules).containsExactly("moduleA", "moduleB")
    }

    @Test
    fun `a change to a module with an incoming dependency returns that module and its incoming dependency`() {
        // Given
        val impactedModuleProvider = ImpactedModuleProvider(moduleGraph)

        // When
        val impactedModules = impactedModuleProvider.getModulesImpactedByChanges("moduleC")

        // Then
        assertThat(impactedModules).containsExactly("moduleB", "moduleC")
    }

    @Test
    fun `a change to a module with an incoming dependency returns all 'ancestor' dependencies`() {
        // Given
        val impactedModuleProvider = ImpactedModuleProvider(moduleGraph)

        // When
        val impactedModules = impactedModuleProvider.getModulesImpactedByChanges("moduleE")

        // Then
        assertThat(impactedModules).containsExactly("moduleB", "moduleD", "moduleE")
    }

    @Test
    fun `a change to a module with multiple incoming dependencies returns all 'ancestor' dependencies`() {
        // Given
        val impactedModuleProvider = ImpactedModuleProvider(moduleGraph)

        // When
        val impactedModules = impactedModuleProvider.getModulesImpactedByChanges("moduleF")

        // Then
        assertThat(impactedModules).containsExactly(
            "moduleA",
            "moduleB",
            "moduleD",
            "moduleF"
        )
    }
}

