package phoenix.world.structures.bunker

import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import phoenix.world.structures.ICompositePieceType
import phoenix.world.structures.IPieceType

enum class CompositePieceType : ICompositePieceType<BunkerProperties>
{
    enternace
    {
        override val variants: List<IPieceType<BunkerProperties>> = listOf(SimplePieceType.entrance_1)
        override fun placeRecursive(inputPos: BlockPos, info: BunkerProperties) {
            TODO("Not yet implemented")
        }

    };
    enum class SimplePieceType : IPieceType<BunkerProperties>
    {
        entrance_1
        {
            override val outputs: List<Pair<BlockPos, Direction>>
                get() = TODO("Not yet implemented")
            override val inputOffset: BlockPos
                get() = TODO("Not yet implemented")

            override fun placeRecursive(inputPos: BlockPos, info: BunkerProperties) {

            }
        };
    }
}