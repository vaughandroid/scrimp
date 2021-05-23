package a

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AFactoryTest {
    @Test
    fun `test A factory`() {
        val result = AFactory().createA()
        assertThat(result).isEqualTo("A")
    }
}