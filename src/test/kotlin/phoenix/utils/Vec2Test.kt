package phoenix.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class Vec2Test
{
    @Test
    fun plus()
    {
        assertEquals(Vec2(2, 3) + Vec2(3, 4), Vec2(5, 7))
    }

    @Test
    fun minus()
    {
        assertEquals(Vec2(5, 7) - Vec2(2, 3), Vec2(3, 4))
        assertEquals(Vec2(5, 7) - Vec2(3, 4), Vec2(2, 3))
    }

    @Test
    fun times()
    {
        assertEquals(Vec2(1, 2) * 2, Vec2(2, 4))
        assertEquals(Vec2(8, 4) * 0.5, Vec2(4, 2))
    }

    @Test
    fun div()
    {
        assertEquals(Vec2(1, 2) / 2, Vec2(0.5, 1))
        assertEquals(Vec2(8, 4) / 0.5, Vec2(16, 8))
    }

    @Test
    operator fun invoke()
    {
        assertEquals(5.0, Vec2(3, 4)())
        assertEquals(5.0, Vec2(4, 3)())
        assertEquals(13.0, Vec2(5, 12)())
    }
}