package phoenix.utils

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import phoenix.other.toWords

internal class StringUtilsTest
{
    @Test
    fun toWords()
    {
        val string = "Sss ss rrr f g h arrr. aaa. Arrrgh\\. aaa!aa a."
        assertEquals(arrayListOf("Sss", "ss", "rrr", "f", "g", "h", "arrr.", "aaa.", "Arrrgh\\.", "aaa!aa", "a."), string.toWords())
    }
}