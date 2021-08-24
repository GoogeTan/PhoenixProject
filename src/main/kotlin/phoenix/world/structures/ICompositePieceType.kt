package phoenix.world.structures

import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.server.ServerWorld

interface ICompositePieceType<T : IPieceProperties>
{
    val variants : Array<out IPieceType>
    val outputs  : Array<ICompositePieceType<T>>

    fun placeRecursive(world : ServerWorld, inputPos: BlockPos, info: T, direction: Direction)
}