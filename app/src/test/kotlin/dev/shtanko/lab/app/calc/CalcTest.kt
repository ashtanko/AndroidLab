package dev.shtanko.lab.app.calc

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CalcTest {

    private lateinit var calc: Calc

    @BeforeEach
    fun setUp() {
        calc = Calc()
    }

    @Test
    fun `positive numbers addition should add numbers correctly`() {
        assertEquals(4, calc.add(2, 2))
    }

    @Test
    fun `negative numbers addition should add numbers correctly`() {
        assertEquals(4, calc.add(-2, -2))
    }
}
