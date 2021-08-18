package phoenix.world.structures.bunker

import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.server.ServerWorld
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.other.nextInt
import phoenix.world.structures.ICompositePieceType
import phoenix.world.structures.IPieceType
import kotlin.math.sqrt

enum class BunkerPieces : ICompositePieceType<BunkerProperties>
{
    Enternace
    {
        override val variants: Array<out IPieceType> = EntrancePieces.values()
        override val outputs: Array<out IPieceType> = HallwayPieces.values()

        override fun placeRecursive(world: ServerWorld, inputPos: BlockPos, info: BunkerProperties)
        {
            val variant = variants[sqrt(world.rand.nextInt(0, variants.size * variants.size).toDouble()).toInt()]
            val pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, inputPos) - variant.inputOffset.first
            variant.place(world, pos)
        }
    },
    Hallway
    {
        override val variants: Array<out IPieceType> = EntrancePieces.values()
        override val outputs: Array<out IPieceType> = HallwayPieces.values()
    };

    override fun placeRecursive(world: ServerWorld, inputPos: BlockPos, info: BunkerProperties)
    {
        val variant = variants[sqrt(world.rand.nextInt(0, variants.size * variants.size).toDouble()).toInt()]
        val pos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, inputPos) - variant.inputOffset.first
        variant.place(world, pos)
    }

    enum class EntrancePieces(override val outputs: List<Pair<BlockPos, Direction>>, override val inputOffset: Pair<BlockPos, Direction>, override val path: ResourceLocation) : IPieceType
    {
        BaseEntrance(listOf(Pair(BlockPos(2, 0, 2), Direction.DOWN)), Pair(BlockPos(2, 5, 2), Direction.UP), ResourceLocation(MOD_ID, "bunker/entrance_1")),
        RareEntrance(listOf(Pair(BlockPos(4, 0, 4), Direction.DOWN)), Pair(BlockPos(4, 6, 4), Direction.UP), ResourceLocation(MOD_ID, "bunker/entrance_3")),
        EpicEntrance(listOf(Pair(BlockPos(2, 0, 2), Direction.DOWN)), Pair(BlockPos(2, 5, 2), Direction.UP), ResourceLocation(MOD_ID, "bunker/entrance_2"));
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
            ResourceLocation(MOD_ID, "bunker/hallway_1")
        ),
        RareHallway
        (
            listOf(Pair(BlockPos(4, 1, 7), Direction.SOUTH)),
            Pair(BlockPos(3, 4, 3), Direction.UP),
            ResourceLocation(MOD_ID, "bunker/hallway_2")
        );
    }

    companion object
    {
        operator fun BlockPos.minus(second: BlockPos): BlockPos
        {
            return BlockPos(x - second.x, y - second.y, z - second.z)
        }
    }
}