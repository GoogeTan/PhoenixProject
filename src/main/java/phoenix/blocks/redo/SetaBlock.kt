package phoenix.blocks.redo

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.fluid.Fluids
import net.minecraft.fluid.IFluidState
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties.AGE_0_3
import net.minecraft.state.properties.BlockStateProperties.WATERLOGGED
import net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.ToolType
import phoenix.Phoenix
import phoenix.init.PhoenixBlocks
import phoenix.utils.block.ICustomGroup
import java.util.*

class SetaBlock : Block(Properties.create(Material.CACTUS).notSolid().tickRandomly().harvestTool(ToolType.SHOVEL).lightValue(5)), IGrowable, ICustomGroup, IWaterLoggable
{
    val SHAPE = makeCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0)

    init
    {
        defaultState = this.stateContainer.baseState
                .with(AGE_0_3, 0)
                .with(WATERLOGGED, false)
                .with(HORIZONTAL_FACING, Direction.EAST)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(AGE_0_3)
        builder.add(WATERLOGGED)
        builder.add(HORIZONTAL_FACING)
        super.fillStateContainer(builder)
    }

    override fun isValidPosition(state: BlockState, worldIn: IWorldReader, pos: BlockPos): Boolean
    {
        return when
        {
            worldIn.getBlockState(pos.up()).block    == PhoenixBlocks.FERTILE_END_STONE.get() -> true
            worldIn.getBlockState(pos.east()).block  == PhoenixBlocks.FERTILE_END_STONE.get() -> true
            worldIn.getBlockState(pos.west()).block  == PhoenixBlocks.FERTILE_END_STONE.get() -> true
            worldIn.getBlockState(pos.north()).block == PhoenixBlocks.FERTILE_END_STONE.get() -> true
            worldIn.getBlockState(pos.south()).block == PhoenixBlocks.FERTILE_END_STONE.get() -> true
            else -> false
        }
    }

    override fun tick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, rand: Random)
    {
        if(worldIn.rand.nextInt(100) == 0 && canGrow(worldIn, pos, state, false))
            grow(worldIn, rand, pos, state)
    }

    override fun canGrow(worldIn: IBlockReader, pos: BlockPos, state: BlockState, isClient: Boolean) = !state.get(WATERLOGGED)
    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: BlockState) = false

    override fun grow(worldIn: ServerWorld, rand: Random, pos: BlockPos, state: BlockState)
    {
        val age = state[AGE_0_3]
        if(age != 3)
            worldIn.setBlockState(pos, state.with(AGE_0_3, age + 1))
    }


    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = SHAPE

    override fun getTab() = Phoenix.REDO

    override fun neighborChanged(state: BlockState, worldIn: World, pos: BlockPos, blockIn: Block, fromPos: BlockPos, isMoving: Boolean)
    {
        worldIn.setBlockState(pos, state.with(WATERLOGGED, worldIn.getFluidState(pos).fluid === Fluids.WATER))
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving)
    }

    override fun getFluidState(state: BlockState): IFluidState
    {
        return if (state.get(WATERLOGGED)) Fluids.WATER.getStillFluidState(false) else super.getFluidState(state)
    }
}
