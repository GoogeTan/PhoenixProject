package phoenix.blocks.redo

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.IGrowable
import net.minecraft.block.material.Material
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties.AGE_0_3
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.ToolType
import phoenix.Phoenix
import phoenix.init.PhoenixBlocks
import phoenix.utils.block.ICustomGroup
import java.util.*

class SetaBlock : Block(Properties.create(Material.CACTUS).notSolid().tickRandomly().harvestTool(ToolType.SHOVEL).lightValue(5)), IGrowable, ICustomGroup
{
    init
    {
        defaultState = this.stateContainer.baseState.with(AGE_0_3, 0)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(AGE_0_3)
        super.fillStateContainer(builder)
    }

    override fun isValidPosition(state: BlockState, worldIn: IWorldReader, pos: BlockPos): Boolean
    {
        return when
        {
            worldIn.getBlockState(pos.up()).block == PhoenixBlocks.FERTILE_END_STONE    -> true
            worldIn.getBlockState(pos.east()).block == PhoenixBlocks.FERTILE_END_STONE  -> true
            worldIn.getBlockState(pos.west()).block == PhoenixBlocks.FERTILE_END_STONE  -> true
            worldIn.getBlockState(pos.north()).block == PhoenixBlocks.FERTILE_END_STONE -> true
            worldIn.getBlockState(pos.south()).block == PhoenixBlocks.FERTILE_END_STONE -> true
            else -> false
        }
    }

    override fun tick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, rand: Random)
    {
        if(worldIn.rand.nextInt(100) == 0 && canGrow(worldIn, pos, state, false))
            grow(worldIn, rand, pos, state)
    }

    override fun canGrow(worldIn: IBlockReader, pos: BlockPos, state: BlockState, isClient: Boolean) = worldIn.getLightValue(pos) < 7
    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: BlockState) = false
    override fun grow(worldIn: ServerWorld, rand: Random, pos: BlockPos, state: BlockState)
    {
        val age = state[AGE_0_3]
        if(age != 3)
            worldIn.setBlockState(pos, state.with(AGE_0_3, age + 1))
    }

    override fun getTab() = Phoenix.REDO
}
