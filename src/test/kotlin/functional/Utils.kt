package functional

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import java.io.File

object Utils {

    fun runGradle(dir: File, vararg args: String): BuildResult {
        return GradleRunner.create()
            .withProjectDir(dir)
            .withPluginClasspath()
            .forwardOutput()
            .withDebug(true)
            .withArguments(args.toMutableList().plus("--stacktrace"))
            .build()
    }

}