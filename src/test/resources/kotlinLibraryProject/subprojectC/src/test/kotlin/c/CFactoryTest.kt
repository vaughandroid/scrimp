package c

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CFactoryTest {
    @Test
    fun `test C factory`() {
        val result = CFactory().createC()
        assertThat(result).isEqualTo("C")
    }
}