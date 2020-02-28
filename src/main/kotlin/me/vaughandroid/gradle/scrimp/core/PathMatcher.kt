package me.vaughandroid.gradle.scrimp.core

import java.nio.file.Path
import java.nio.file.Paths

class PathMatcher(
    private val folderPaths: Set<Path>
) {

    constructor(vararg folderPaths: Path) : this(folderPaths.toSet())

    constructor(vararg folderPathStrings: String) :
            this(folderPathStrings.map { Paths.get(it) }.toSet())

    fun findClosestParent(pathString: String): Path? =
        findClosestParent(Paths.get(pathString))

    fun findClosestParent(path: Path): Path? =
        folderPaths
            .filter { folderPath ->
                path.startsWith(folderPath)
            }
            .maxBy { it.nameCount }

}