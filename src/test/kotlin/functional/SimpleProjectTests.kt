package functional

import org.junit.Rule
import org.junit.Test

class SimpleProjectTests {

    @get:Rule
    val functionalTestHelper = FunctionalTestHelper("simpleProject")

    @Test
    fun `The project can be built`() {
        val buildResult = functionalTestHelper.runGradle()

        functionalTestHelper.assertAllTasksSuccessful(buildResult)
    }
}
