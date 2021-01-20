package me.vaughandroid.gradle.scrimp

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Test
import java.io.File

class FunctionalTests {

    private val tempDir: File = createTempDir()

    @Before
    fun setup() {
        val srcDir = "$tempDir/src/main/kotlin"
        File(srcDir).mkdirs()
    }

    @Test
    fun `Plugin can be applied to a build file`() {
        createBuildFiles(buildFile, tempDir)
        runGradle(tempDir)
    }
}


const val buildFile = """
plugins {
    id 'scrimp'
}
"""

private fun createBuildFiles(buildFile: String, dir: File) {
    File(dir, "build.gradle").apply {
        writeText(buildFile)
    }
}

private fun runGradle(dir: File, vararg args: String): BuildResult {
    return GradleRunner.create()
        .withProjectDir(dir)
        .withPluginClasspath()
        .forwardOutput()
        .withDebug(true)
        .withArguments(args.toMutableList().plus("--stacktrace"))
        .build()
}

