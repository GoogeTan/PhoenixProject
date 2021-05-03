package phoenix.utils.block

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader

abstract class BlockWithTile(properties: Properties) : Block(properties)
{
    override fun hasTileEntity(state: BlockState) = true

    abstract override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity
}