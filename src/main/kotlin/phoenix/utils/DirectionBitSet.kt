package phoenix.utils

import net.minecraft.util.Direction
import java.util.*

class DirectionBitSet
{
    private val info = BitSet(6)

    operator fun get(direction: Direction) = info[direction.index]
    operator fun set(direction: Direction, boolean: Boolean) {
        info[direction.index] = boolean
    }

    private fun sum(): Int
    {
        var res = 0
        info.stream().forEach { bit -> res += bit }
        return res
    }

    fun isEmpty(): Boolean = sum() == 0
}