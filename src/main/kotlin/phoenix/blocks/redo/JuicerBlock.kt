package phoenix.blocks.redo

import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader
import phoenix.utils.block.BlockWithTile
import phoenix.utils.block.IRedoThink

object JuicerBlock : BlockWithTile(Properties.create(Material.ROCK).lightValue(5).notSolid().hardnessAndResistance(3.0f)), IRedoThink
{
    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity?
    {
        TODO("Not yet implemented")
    }
}