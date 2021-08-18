package phoenix.world.structures

import net.minecraft.util.Direction
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.feature.template.PlacementSettings
import net.minecraft.world.server.ServerWorld
import phoenix.other.*

interface IPieceType
{
    val outputs : List<Pair<BlockPos, Direction>>
    val inputOffset : Pair<BlockPos, Direction>
    val path : ResourceLocation

    fun place(world : ServerWorld, pos : BlockPos, settings: PlacementSettings = PlacementSettings())
    {
        val template =  world.structureTemplateManager.getTemplate(path)
        if (template != null)
        {
            template.addBlocksToWorld(world, pos.add(inputOffset.first), settings)
            debug("<Other events> ", "Corn genned ^)")
        }
        else
        {
            error("<Other events> ", "Corn was not genned ^(. template is null... I it is very bad think.")
        }
    }
}