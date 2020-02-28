package me.vaughandroid.gradle.scrimp.core

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.nio.file.Paths

class PathMatcherTests {

    @Test
    fun `an exact match is a match`() {
        // Given
        val searchPath = Paths.get("a/b/c")
        val pathMatcher = PathMatcher("a/b/c")

        // When
        val actualPath = pathMatcher.findClosestParent(searchPath)

        // Then
        assertThat(actualPath).isEqualTo(searchPath)
    }

    @Test
    fun `files within a folder are matches`() {
        // Given
        val folderPath = Paths.get("a/b")
        val pathMatcher = PathMatcher(folderPath)

        // When
        val actualPath = pathMatcher.findClosestParent("a/b/file.txt")

        // Then
        assertThat(actualPath).isEqualTo(folderPath)
    }

    @Test
    fun `subfolders are matches`() {
        // Given
        val folderPath = Paths.get("a/b")
        val pathMatcher = PathMatcher(folderPath)

        // When
        val actualPath = pathMatcher.findClosestParent("a/b/c/d")

        // Then
        assertThat(actualPath).isEqualTo(folderPath)
    }

    @Test
    fun `deeply nested files are matches`() {
        // Given
        val folderPath = Paths.get("a/b")
        val pathMatcher = PathMatcher(folderPath)

        // When
        val actualPath = pathMatcher.findClosestParent("a/b/c/d/e/foo.txt")

        // Then
        assertThat(actualPath).isEqualTo(folderPath)
    }

    @Test
    fun `deeply nested subfolders are matches`() {
        // Given
        val folderPath = Paths.get("a/b")
        val pathMatcher = PathMatcher(folderPath)

        // When
        val actualPath = pathMatcher.findClosestParent("a/b/c/d/e/f")

        // Then
        assertThat(actualPath).isEqualTo(folderPath)
    }

    @Test
    fun `a completely different path is not a match`() {
        // Given
        val pathMatcher = PathMatcher("a/b/c")

        // When
        val actualPath = pathMatcher.findClosestParent("x/y/z")

        // Then
        assertThat(actualPath).isNull()
    }

    @Test
    fun `paths that differ by a single element are not a match`() {
        // Given
        val pathMatcher = PathMatcher("a/b/c")

        // When
        val actualPath1 = pathMatcher.findClosestParent("x/b/c")
        val actualPath2 = pathMatcher.findClosestParent("a/x/c")
        val actualPath3 = pathMatcher.findClosestParent("a/b/x")

        // Then
        assertThat(actualPath1).isNull()
        assertThat(actualPath2).isNull()
        assertThat(actualPath3).isNull()
    }

    @Test
    fun `if there are multiple matches, the closest is returned`() {
        // Given
        val pathMatcher = PathMatcher("a", "a/b", "a/b/c", "a/b/c/d")

        // When
        val actualPath = pathMatcher.findClosestParent("a/b/c/x")

        // Then
        assertThat(actualPath).isEqualTo(Paths.get("a/b/c"))
    }

    @Test
    fun `attempting to match a file with a shorter path than any of the folder paths is not a match`() {
        // Given
        val pathMatcher = PathMatcher("a/b", "a/b/c", "a/b/c/d")

        // When
        val actualPath = pathMatcher.findClosestParent("file.txt")

        // Then
        assertThat(actualPath).isNull()
    }

    @Test
    fun `absolute paths are supported`() {
        // Given
        val searchPath = Paths.get("/a/b/c")
        val pathMatcher = PathMatcher("/a/b/c")

        // When
        val actualPath = pathMatcher.findClosestParent(searchPath)

        // Then
        assertThat(actualPath).isEqualTo(searchPath)
    }

}