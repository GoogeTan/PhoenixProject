package phoenix.api.block

import net.minecraft.block.BlockState
import net.minecraft.block.ContainerBlock
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockReader

abstract class BlockWithContainer(properties: Properties) : ContainerBlock(properties)
{
    override fun hasTileEntity(state: BlockState) = true

    final override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = createNewTileEntity(world)
    abstract override fun createNewTileEntity(worldIn: IBlockReader): TileEntity
}