package root

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RootFactoryTest {
    @Test
    fun `test Root factory`() {
        val result = RootFactory().createRoot()
        assertThat(result).isEqualTo("Root")
    }
}