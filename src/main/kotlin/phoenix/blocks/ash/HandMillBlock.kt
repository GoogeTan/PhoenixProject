package phoenix.blocks.ash

import net.minecraft.block.BlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader
import phoenix.api.block.BlockWithTile

class HandMillBlock(properties: Properties) : BlockWithTile(properties)
{
    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity {
        TODO("Not yet implemented")
    }
}