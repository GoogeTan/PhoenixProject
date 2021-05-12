package phoenix.blocks.redo

import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.item.ItemGroup
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import phoenix.Phoenix
import phoenix.tile.redo.BambooPipeTile
import phoenix.utils.block.ICustomGroup

class VerticalBambooPipeBlock : Block(Properties.create(Material.WOOD).notSolid().hardnessAndResistance(1.0f)), ICustomGroup
{
    private val NORMAL = makeCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0)

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = BambooPipeTile()

    override fun isNormalCube(state: BlockState, worldIn: IBlockReader, pos: BlockPos): Boolean = false

    override fun hasTileEntity(state: BlockState?): Boolean = true

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = NORMAL

    override val tab: ItemGroup = Phoenix.REDO
}
