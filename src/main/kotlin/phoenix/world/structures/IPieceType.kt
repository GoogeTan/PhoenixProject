package phoenix.world.structures

import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos

interface IPieceType<T : IPieceProperties>
{
    val outputs : List<Pair<BlockPos, Direction>>
    val inputOffset : Pair<BlockPos, Direction>
    fun placeRecursive(inputPos : BlockPos, info : T)
}