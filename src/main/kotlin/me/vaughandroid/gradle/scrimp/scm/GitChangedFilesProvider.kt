package me.vaughandroid.gradle.scrimp.scm

import org.gradle.api.Project
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import java.nio.file.Paths

class GitChangedFilesProvider(
    project: Project
) : ChangedFilesProvider {

    private val rootProject = project.rootProject

    /**
     * @param commitReference Commit SHA, or e.g. HEAD~7
     */
    override fun getChangedFiles(commitReference: String): Set<Path> {
        val gitProjectRoot = getGitProjectRoot()
        val outputStream = ByteArrayOutputStream()
        rootProject.exec {
            workingDir = gitProjectRoot.toFile()
            standardOutput = outputStream
            commandLine = listOf("git", "diff", "--name-only", commitReference)
        }
        val relativeFilenames = outputStream.toString().split('\n').filter { it.isNotBlank() }

        return relativeFilenames
            .map { gitProjectRoot.resolve(it) }
            .toSet()
    }

    private fun getGitProjectRoot() : Path {
        val outputStream = ByteArrayOutputStream()
        rootProject.exec {
            workingDir = rootProject.projectDir
            standardOutput = outputStream
            commandLine = listOf("git", "rev-parse", "--show-toplevel")
        }
        return Paths.get(outputStream.toString().trim())
    }
}