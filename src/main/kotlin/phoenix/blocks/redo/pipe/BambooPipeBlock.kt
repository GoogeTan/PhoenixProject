package phoenix.blocks.redo.pipe

import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.ItemGroup
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import phoenix.Phoenix.Companion.REDO
import phoenix.api.block.ICustomGroup
import phoenix.tile.redo.pipe.BambooPipeTile

object BambooPipeBlock : Block(Properties.create(Material.WOOD).notSolid().hardnessAndResistance(1.0f)), ICustomGroup
{
    private val NORMAL = makeCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0)

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = BambooPipeTile()

    override fun isNormalCube(state: BlockState, worldIn: IBlockReader, pos: BlockPos): Boolean = false

    override fun hasTileEntity(state: BlockState?): Boolean = true

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = NORMAL

    override val tab: ItemGroup = REDO

    init
    {
        defaultState = defaultState.with(HORIZONTAL_FACING, Direction.NORTH)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        super.fillStateContainer(builder)
        builder.add(HORIZONTAL_FACING)
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState = defaultState.with(HORIZONTAL_FACING, context.placementHorizontalFacing)
}
