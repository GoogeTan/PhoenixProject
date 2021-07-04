package phoenix.world.structures

import net.minecraft.util.math.BlockPos

interface ICompositePieceType<T : IPieceProperties>
{
    val variants : List<IPieceType<T>>
    fun placeRecursive(inputPos: BlockPos, info: T)
}