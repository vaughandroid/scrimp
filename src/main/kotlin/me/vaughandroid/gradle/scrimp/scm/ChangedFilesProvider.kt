package me.vaughandroid.gradle.scrimp.scm

import java.nio.file.Path

interface ChangedFilesProvider {

    fun getChangedFiles(commitReference: String) : Set<Path>

}