package phoenix.utils

import net.minecraft.util.math.BlockPos
import kotlin.math.sqrt

object BlockPosUtils
{
    operator fun BlockPos.minus(second: BlockPos) : Double
    {
        return sqrt(((x - second.x) * (x - second.x) + (x - second.y) * (y - second.y) + (x - second.z) * (z - second.z)).toDouble())
    }

    fun isNear(pos: BlockPos, poses: Collection<BlockPos>, range: Int): Boolean
    {
        for (pos1 in poses) if (pos - pos1 < range) return false
        return true
    }
}