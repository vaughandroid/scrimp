package functional

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class SimpleProjectTests {

    @get:Rule
    val tempFolderRule = TemporaryFolder()

    private val sourceProject = File(javaClass.getResource("/simpleProject/").toURI())

    @Before
    fun setup() {
        sourceProject.copyRecursively(tempFolderRule.root)
    }

    @Test
    fun `The project can be built`() {
        Utils.runGradle(tempFolderRule.root)
    }
}
