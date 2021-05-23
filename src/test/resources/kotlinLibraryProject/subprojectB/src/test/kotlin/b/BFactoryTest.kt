package b

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class BFactoryTest {
    @Test
    fun `test B factory`() {
        val result = BFactory().createB()
        assertThat(result).isEqualTo("B")
    }
}