package phoenix.blocks.redo.pipe

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.item.ItemGroup
import net.minecraft.state.StateContainer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import phoenix.Phoenix
import phoenix.tile.redo.pipe.VerticalBambooPipeTile
import phoenix.utils.block.ICustomGroup

object VerticalBambooPipeBlock : RotatedPillarBlock(Properties.create(Material.WOOD).notSolid().hardnessAndResistance(1.0f)), ICustomGroup
{
    private val NORMAL = makeCuboidShape(4.0, 0.0, 4.0, 12.0, 16.0, 12.0)

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = VerticalBambooPipeTile()

    override fun isNormalCube(state: BlockState, worldIn: IBlockReader, pos: BlockPos): Boolean = false

    override fun hasTileEntity(state: BlockState?): Boolean = true

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = NORMAL

    override val tab: ItemGroup = Phoenix.REDO
}