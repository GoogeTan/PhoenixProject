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
                get() = listOf(Pair(BlockPos(2, 0, 2), Direction.DOWN))
            override val inputOffset: Pair<BlockPos, Direction>
                get() = Pair(BlockPos(2, 5, 2), Direction.UP)

            override fun placeRecursive(inputPos: BlockPos, info: BunkerProperties) {

            }
        },
        entrance_2
        {
            override val outputs: List<Pair<BlockPos, Direction>>
            get() = listOf(Pair(BlockPos(2, 0, 2), Direction.DOWN))
            override val inputOffset: Pair<BlockPos, Direction>
            get() = Pair(BlockPos(2, 5, 2), Direction.UP)

            override fun placeRecursive(inputPos: BlockPos, info: BunkerProperties) {

            }
        },
        entrance_3
        {
            override val outputs: List<Pair<BlockPos, Direction>>
                get() = listOf(Pair(BlockPos(4, 0, 4), Direction.DOWN))
            override val inputOffset: Pair<BlockPos, Direction>
                get() = Pair(BlockPos(4, 6, 4), Direction.UP)

            override fun placeRecursive(inputPos: BlockPos, info: BunkerProperties) {

            }
        },
        hallway_1
        {
            override val outputs: List<Pair<BlockPos, Direction>>
                get() = listOf(
                    Pair(BlockPos(2, 1, 0), Direction.NORTH),
                    Pair(BlockPos(0, 1, 3), Direction.WEST),
                    Pair(BlockPos(3, 1, 5), Direction.SOUTH),
                    Pair(BlockPos(5, 1, 2), Direction.EAST)
                )
            override val inputOffset: Pair<BlockPos, Direction>
                get() = Pair(BlockPos(2, 4, 2), Direction.UP)

            override fun placeRecursive(inputPos: BlockPos, info: BunkerProperties) {

            }
        },
        hallway_2
        {
            override val outputs: List<Pair<BlockPos, Direction>>
                get() = listOf(
                    Pair(BlockPos(4, 1, 7), Direction.SOUTH)
                )
            override val inputOffset: Pair<BlockPos, Direction>
                get() = Pair(BlockPos(3, 4, 3), Direction.UP)

            override fun placeRecursive(inputPos: BlockPos, info: BunkerProperties) {

            }
        };
    }
}