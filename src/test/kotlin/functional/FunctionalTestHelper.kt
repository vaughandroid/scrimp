package functional

import com.google.common.truth.Truth.assertWithMessage
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.rules.TemporaryFolder
import java.io.File

class FunctionalTestHelper(
    projectResourceFolderName: String
) : TemporaryFolder() {

private val sourceProject = File(javaClass.getResource("/$projectResourceFolderName/").toURI())

    override fun before() {
        super.before()

        sourceProject.copyRecursively(root)
    }

    fun runGradle(vararg args: String) {
        val buildResult = GradleRunner.create()
            .withProjectDir(root)
            .withPluginClasspath()
            .forwardOutput()
            .withDebug(true)
            .withArguments(args.toMutableList().plus("--stacktrace"))
            .build()
    }

}