package me.vaughandroid.gradle.scrimp.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.nio.file.Path
import java.nio.file.Paths

class ChangedModuleProviderTests {

    private val moduleGraph = ModuleGraph("rootProject").apply {
        addModule("rootModuleA")
        addModule("rootModuleB")
        addDependency("rootModuleB", "topLevelSubmodule")
        addDependency("rootModuleB", "submoduleWithinModule")
    }

    private val projectPathProvider = object : ProjectPathProvider {
        override fun getPathForProject(projectName: String): Path =
            Paths.get(
                when (projectName) {
                    "submoduleWithinModule" -> "rootModuleB/submoduleWithinModule"
                    else -> projectName
                }
            )
    }

    @Test
    fun `a module with a change in it is included in the set of changed modules`() {
        // Given
        val changedFilePaths = setOf(Paths.get("rootModuleA/file.txt"))
        val changedModuleProvider = ChangedModuleProvider(projectPathProvider, moduleGraph)

        // When
        val changedModules =
            changedModuleProvider.getModulesWithChanges(changedFilePaths)

        // Then
        assertThat(changedModules).containsExactly("rootModuleA")
    }

    @Test
    fun `a module with a change in a subfolder is included in the set of changed modules`() {
        // Given
        val changedFilePaths = setOf(Paths.get("rootModuleA/foo/bar/file.txt"))
        val changedModuleProvider = ChangedModuleProvider(projectPathProvider, moduleGraph)

        // When
        val changedModules =
            changedModuleProvider.getModulesWithChanges(changedFilePaths)

        // Then
        assertThat(changedModules).containsExactly("rootModuleA")
    }

    @Test
    fun `a submodule with a change in it is included in the set of changed modules`() {
        // Given
        val changedFilePaths = setOf(Paths.get("topLevelSubmodule/file.txt"))
        val changedModuleProvider = ChangedModuleProvider(projectPathProvider, moduleGraph)

        // When
        val changedModules =
            changedModuleProvider.getModulesWithChanges(changedFilePaths)

        // Then
        assertThat(changedModules).containsExactly("topLevelSubmodule")
    }

    @Test
    fun `a nested submodule with a change in it is included in the set of changed modules, but the containing project is not`() {
        // Given
        val changedFilePaths = setOf(Paths.get("rootModuleB/submoduleWithinModule/file.txt"))
        val changedModuleProvider = ChangedModuleProvider(projectPathProvider, moduleGraph)

        // When
        val changedModules =
            changedModuleProvider.getModulesWithChanges(changedFilePaths)

        // Then
        assertThat(changedModules).containsExactly("submoduleWithinModule")
    }

    @Test
    fun `if a change does not match any module paths is it assigned to the root project`() {
        // Given
        val changedFilePaths = setOf(Paths.get("foo/bar/file.txt"))
        val changedModuleProvider = ChangedModuleProvider(projectPathProvider, moduleGraph)

        // When
        val changedModules =
            changedModuleProvider.getModulesWithChanges(changedFilePaths)

        // Then
        assertThat(changedModules).containsExactly("rootProject")
    }

    @Test
    fun `all modules with changes are returned`() {
        // Given
        val changedFilePaths = setOf(
            Paths.get("file.txt"),
            Paths.get("topLevelSubmodule/file2.txt"),
            Paths.get("rootModuleA/file3.json"),
            Paths.get("rootModuleB/subfolder/file4.xml")
        )
        val changedModuleProvider = ChangedModuleProvider(projectPathProvider, moduleGraph)

        // When
        val changedModules =
            changedModuleProvider.getModulesWithChanges(changedFilePaths)

        // Then
        assertThat(changedModules).containsExactly(
            "rootProject",
            "topLevelSubmodule",
            "rootModuleA",
            "rootModuleB"
        )
    }

}

