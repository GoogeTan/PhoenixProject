package phoenix.blocks.redo.cable

import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader
import phoenix.api.block.BlockWithTile

object StealCableBlock : BlockWithTile(Properties.create(Material.IRON).sound(SoundType.CORAL).hardnessAndResistance(4F).notSolid())
{
    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity
    {
        TODO("Not yet implemented")
    }
}