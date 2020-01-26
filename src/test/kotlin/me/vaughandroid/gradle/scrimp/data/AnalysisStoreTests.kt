package me.vaughandroid.gradle.scrimp.data

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.nio.file.Path

class AnalysisStoreTests {

    @Rule
    @JvmField
    val temporaryFolder = TemporaryFolder()

    @Test
    fun `reading when no data is stored returns null`() {
        // Given
        val filePath = getPathForTemporaryFile()
        val store = AnalysisStore(filePath)

        // When
        val readData = store.retrieve()

        // Then
        assertThat(readData).isNull()
    }

    @Test
    fun `analysis data can be stored and retrieved`() {
        // Given
        val filePath = getPathForTemporaryFile()
        val store = AnalysisStore(filePath)
        val dataToStore = AnalysisData(
            commitRef = "HEAD~7",
            modulesWithChanges = setOf("moduleA", "moduleB"),
            modulesImpactedByChanges = setOf("moduleA", "moduleB", "moduleC")
        )

        // When
        store.store(dataToStore)
        val readData = store.retrieve()

        // Then
        assertThat(readData).isEqualTo(dataToStore)
    }

    @Test
    fun `analysis data can be stored and retrieved by different stores`() {
        // Given
        val filePath = getPathForTemporaryFile()
        val writeStore = AnalysisStore(filePath)
        val readStore = AnalysisStore(filePath)
        val dataToStore = AnalysisData(
            commitRef = "HEAD~7",
            modulesWithChanges = setOf("moduleA", "moduleB"),
            modulesImpactedByChanges = setOf("moduleA", "moduleB", "moduleC")
        )

        // When
        writeStore.store(dataToStore)
        val readData = readStore.retrieve()

        // Then
        assertThat(readData).isEqualTo(dataToStore)
    }

    private fun getPathForTemporaryFile(): Path =
        temporaryFolder.newFile().toPath()

}

