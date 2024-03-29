package phoenix.blocks.ash

import net.minecraft.block.BlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader
import net.minecraftforge.common.ToolType
import phoenix.api.block.BlockWithTile
import phoenix.tile.ash.SolarDryerTile

class SolarDryerBlock(properties: Properties) : BlockWithTile(properties.notSolid().hardnessAndResistance(3.0f).harvestTool(ToolType.AXE))
{
    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = SolarDryerTile()
}