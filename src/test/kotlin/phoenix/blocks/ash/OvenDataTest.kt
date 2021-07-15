package phoenix.blocks.ash

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class OvenDataTest
{
    @Test
    fun replace()
    {
        val replaceable = OvenData(5)
        val replacer = OvenData(4)
        replaceable replace replacer
        assert(!(replaceable === replacer))
        assert(replaceable == replacer)
    }

    @Test
    fun tickFire()
    {
    }

    @Test
    fun tickItems()
    {
    }

    @Test
    fun serializeNBT()
    {
    }

    @Test
    fun deserializeNBT()
    {
    }

    @Test
    fun writeToBuf()
    {
    }

    @Test
    fun readFromBuf()
    {
    }
}