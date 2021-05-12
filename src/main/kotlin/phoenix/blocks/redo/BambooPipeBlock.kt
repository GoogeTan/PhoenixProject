package phoenix.blocks.redo

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.fluid.Fluids
import net.minecraft.fluid.IFluidState
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.ItemGroup
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import phoenix.Phoenix.Companion.REDO
import phoenix.tile.redo.BambooPipeTile
import phoenix.utils.block.ICustomGroup

object BambooPipeBlock : HorizontalBlock(Properties.create(Material.WOOD).notSolid().hardnessAndResistance(1.0f)), IWaterLoggable, ICustomGroup
{
    private val NORMAL = makeCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0)

    init
    {
        defaultState = defaultState.with(BlockStateProperties.WATERLOGGED, false)
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState
    {
        return super.getStateForPlacement(context)!!.with(BlockStateProperties.WATERLOGGED, context.world.getFluidState(context.pos).fluid === Fluids.WATER)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        super.fillStateContainer(builder)
        builder.add(BlockStateProperties.WATERLOGGED)
    }

    override fun neighborChanged(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        blockIn: Block,
        fromPos: BlockPos,
        isMoving: Boolean
    )
    {
        worldIn.setBlockState(pos, state.with(BlockStateProperties.WATERLOGGED, worldIn.getFluidState(pos).fluid === Fluids.WATER))
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving)
    }

    override fun getFluidState(state: BlockState): IFluidState
            = if (state.get(BlockStateProperties.WATERLOGGED)) Fluids.WATER.getStillFluidState(false) else super.getFluidState(state)

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = BambooPipeTile()

    override fun isNormalCube(state: BlockState, worldIn: IBlockReader, pos: BlockPos): Boolean = false

    override fun hasTileEntity(state: BlockState?): Boolean = true

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = NORMAL

    override val tab: ItemGroup = REDO
}
