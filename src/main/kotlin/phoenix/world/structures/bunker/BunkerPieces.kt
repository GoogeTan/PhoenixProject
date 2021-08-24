package phoenix.world.structures.bunker

import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.server.ServerWorld
import phoenix.Phoenix
import phoenix.other.LogManager
import phoenix.other.nextInt
import phoenix.world.structures.ICompositePieceType
import phoenix.world.structures.IPieceType
import kotlin.math.sqrt

enum class BunkerPieces : ICompositePieceType<BunkerProperties>
{
    Tunnel
    {
        override val variants: Array<out IPieceType> = EntrancePieces.values()
        override val outputs: Array<ICompositePieceType<BunkerProperties>> = arrayOf(this)
    },
    Hallway
    {
        override val variants: Array<out IPieceType> = HallwayPieces.values()
        override val outputs: Array<ICompositePieceType<BunkerProperties>> = arrayOf(Tunnel)
    },
    Enternace
    {
        override val variants: Array<out IPieceType> = EntrancePieces.values()
        override val outputs: Array<ICompositePieceType<BunkerProperties>> = arrayOf(Hallway)

        override fun placeRecursive(world: ServerWorld, inputPos: BlockPos, info: BunkerProperties, direction: Direction)
        {
            val pos = BlockPos.Mutable(inputPos)
            while(world.isAirBlock(pos))
                pos.move(Direction.DOWN)
            super.placeRecursive(world, pos, info, direction)
        }
    };

    override fun placeRecursive(world: ServerWorld, inputPos: BlockPos, info: BunkerProperties, direction: Direction)
    {
        val variant = variants[sqrt(world.rand.nextInt(0, (variants.size - 1) * (variants.size - 1)).toDouble()).toInt()]
        variant.place(world, inputPos, direction)
        val props = BunkerProperties(info, deep = info.deep - 1)
        try {
            if (props.deep >= 0)
            {
                for ((pos, dir) in variant.outputs)
                {
                    outputs[sqrt(world.rand.nextInt(0, (outputs.size - 1) * (outputs.size - 1)).toDouble()).toInt()]
                        .placeRecursive(
                            world,
                            inputPos + pos - variant.inputOffset.first,
                            props,
                            dir
                        )
                }
            }
        }
        catch (e : Exception)
        {
            LogManager.error("${this::class} $e")
        }
    }

    enum class EntrancePieces(override val outputs: List<Pair<BlockPos, Direction>>, override val inputOffset: Pair<BlockPos, Direction>, override val path: ResourceLocation) : IPieceType
    {
        BaseEntrance(listOf(Pair(BlockPos(2, 0, 2), Direction.DOWN)), Pair(BlockPos(2, 5, 2), Direction.UP), ResourceLocation(Phoenix.MOD_ID, "bunker/entrance_1")),
        RareEntrance(listOf(Pair(BlockPos(4, 0, 4), Direction.DOWN)), Pair(BlockPos(4, 6, 4), Direction.UP), ResourceLocation(Phoenix.MOD_ID, "bunker/entrance_3")),
        EpicEntrance(listOf(Pair(BlockPos(2, 0, 2), Direction.DOWN)), Pair(BlockPos(2, 5, 2), Direction.UP), ResourceLocation(Phoenix.MOD_ID, "bunker/entrance_2"));
    }

    enum class HallwayPieces(override val outputs: List<Pair<BlockPos, Direction>>, override val inputOffset: Pair<BlockPos, Direction>, override val path: ResourceLocation) : IPieceType
    {
        BaseHallway
        (
            listOf(
                Pair(BlockPos(2, 1, 0), Direction.NORTH),
                Pair(BlockPos(0, 1, 3), Direction.WEST),
                Pair(BlockPos(3, 1, 5), Direction.SOUTH),
                Pair(BlockPos(5, 1, 2), Direction.EAST)
                  ),
            Pair(BlockPos(2, 4, 2), Direction.UP),
            ResourceLocation(Phoenix.MOD_ID, "bunker/hallway_1")
        ),
        RareHallway
        (
            listOf(Pair(BlockPos(4, 1, 7), Direction.SOUTH)),
            Pair(BlockPos(3, 4, 3), Direction.UP),
            ResourceLocation(Phoenix.MOD_ID, "bunker/hallway_2")
        );
    }

    companion object
    {
        operator fun BlockPos.minus(second: BlockPos): BlockPos
        {
            return BlockPos(x - second.x, y - second.y, z - second.z)
        }

        operator fun BlockPos.plus(second: BlockPos): BlockPos
        {
            return BlockPos(x + second.x, y + second.y, z + second.z)
        }
    }
}