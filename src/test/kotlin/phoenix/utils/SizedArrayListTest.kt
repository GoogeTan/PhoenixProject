package phoenix.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SizedArrayListTest
{
    @Test
    fun resize()
    {
        val list = sizedArrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        list.resize(4) { 0 }
        assertEquals(list.size, 4)
        assertEquals(list, sizedArrayListOf(1, 2, 3, 4))
        list.resize(10) { index -> index + 1 }
        assertEquals(list.size, 10)
        assertEquals(list, sizedArrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        list.resize(0) { index -> index + 1 }
        assertEquals(list.size, 0)
        assertEquals(list, sizedArrayListOf<Int>())
    }

    @Test
    fun testEquals()
    {
        assertEquals(sizedArrayListOf(0, 1, 2, 3, 4), setOf(0, 1, 2, 3, 4))
        assertEquals(sizedArrayListOf(0, 1, 2, 3, 4), listOf(0, 1, 2, 3, 4))
        assertEquals(sizedArrayListOf(0, 1, 2, 3, 4), sizedArrayListOf(0, 1, 2, 3, 4))
        assert(!sizedArrayListOf(0, 1, 2, 3, 4).equals(null))
    }
}