package phoenix.world.structures

import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.feature.template.PlacementSettings
import net.minecraft.world.server.ServerWorld
import phoenix.other.LogManager
import phoenix.other.rotationOf

interface IPieceType
{
    val outputs : List<Pair<BlockPos, Direction>>
    val inputOffset : Pair<BlockPos, Direction>
    val path : ResourceLocation

    fun place(world: ServerWorld, pos: BlockPos, dir: Direction, settings: PlacementSettings = PlacementSettings())
    {
        settings.rotation = rotationOf(dir)
        LogManager.error("$pos ${inputOffset.first.rotate(rotationOf(dir))}")
        val place = pos - inputOffset.first.rotate(rotationOf(dir))
        LogManager.error(place.toString())
        pos.rotate(rotationOf(dir))
        world.structureTemplateManager.getTemplate(path)?.addBlocksToWorld(world, place, settings)
    }

    companion object
    {
        operator fun BlockPos.minus(second: BlockPos): BlockPos
        {
            return BlockPos(x - second.x, y - second.y, z - second.z)
        }
    }
}