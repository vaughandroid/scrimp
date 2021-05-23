package functional

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.concurrent.TimeUnit.SECONDS

class FunctionalTestHelper(
    projectResourceFolderName: String
) : TemporaryFolder() {

private val sourceProject = File(javaClass.getResource("/$projectResourceFolderName/").toURI())

    override fun before() {
        super.before()

        sourceProject.copyRecursively(root)
        runShellCommand("git init")
        runShellCommand("git add .")
        runShellCommand("git commit -m 'initial_commit'")
    }

    fun runGradle(vararg args: String): BuildResult {
        return GradleRunner.create()
            .withProjectDir(root)
            .withPluginClasspath()
            .forwardOutput()
            .withDebug(true)
            .withArguments(args.toMutableList().plus("--stacktrace"))
            .build()
    }

    fun runShellCommand(command: String) {
        ProcessBuilder(command.split(" "))
            .directory(root)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor(1, SECONDS)
    }

}