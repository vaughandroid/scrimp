package functional

import org.junit.Rule
import org.junit.Test

class KotlinProjectTests {

    @get:Rule
    val functionalTestHelper = FunctionalTestHelper("kotlinLibraryProject")

    @Test
    fun `The project can be built`() {
        functionalTestHelper.apply {
            runGradle("build")
        }
    }

    @Test
    fun `The project can run tests`() {
        functionalTestHelper.apply {
            runGradle("test")
        }
    }

}
