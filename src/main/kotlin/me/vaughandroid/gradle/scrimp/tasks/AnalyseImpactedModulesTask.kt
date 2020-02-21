package me.vaughandroid.gradle.scrimp.tasks


import me.vaughandroid.gradle.scrimp.GradleWrapper
import me.vaughandroid.gradle.scrimp.Logger
import me.vaughandroid.gradle.scrimp.core.ChangedModuleProvider
import me.vaughandroid.gradle.scrimp.core.ImpactedModuleProvider
import me.vaughandroid.gradle.scrimp.core.ModuleGraphBuilder
import me.vaughandroid.gradle.scrimp.data.AnalysisData
import me.vaughandroid.gradle.scrimp.data.AnalysisStore
import me.vaughandroid.gradle.scrimp.scm.GitChangedFilesProvider
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.nio.file.Path

open class AnalyseImpactedModulesTask : DefaultTask() {

    init {
        // The set of changed files might be different, even if the commit reference isn't.
        outputs.upToDateWhen { false }
    }

    @Input
    val commitReference = project.properties["scrimpCommit"]?.toString() ?: "HEAD"

    @OutputFile
    lateinit var analysisDataFilePath: Path

    lateinit var logger: Logger

    @TaskAction
    fun action() {
        val gradleWrapper = GradleWrapper(project)
        val changedFilesProvider = GitChangedFilesProvider(project)
        val moduleGraph = ModuleGraphBuilder(logger).build(gradleWrapper, gradleWrapper)
        val changedModuleProvider = ChangedModuleProvider(gradleWrapper, moduleGraph)
        val impactedModuleProvider = ImpactedModuleProvider(moduleGraph)

        val allModulesAndPaths =
            gradleWrapper.moduleNames.map { it to gradleWrapper.getPathForProject(it) }.toSet()
        logger.logList(allModulesAndPaths, "Modules:")

        val changedFilePaths = changedFilesProvider.getChangedFiles(commitReference)
        logger.logList(changedFilePaths, "Files changed since '$commitReference':")

        val changedModules = changedModuleProvider.getModulesWithChanges(changedFilePaths)
        logger.logList(changedModules, "Modules with changes since '$commitReference':")

        val impactedModules = impactedModuleProvider.getModulesImpactedByChanges(changedModules)
        logger.logList(impactedModules, "Modules impacted by changes since '$commitReference':")

        logger.log("Writing to $analysisDataFilePath...")
        val analysisData = AnalysisData(
            commitRef = commitReference,
            modulesWithChanges = changedModules,
            modulesImpactedByChanges = impactedModules
        )
        AnalysisStore(analysisDataFilePath).store(analysisData)
    }

}

