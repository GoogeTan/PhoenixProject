package phoenix.blocks.redo

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ChorusPlantBlock
import net.minecraft.block.SixWayBlock
import net.minecraft.block.material.Material
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import phoenix.init.PhoenixBlocks

class KikinStemBlock : ChorusPlantBlock(Properties.create(Material.ROCK).notSolid())
{
    override fun makeConnections(reader: IBlockReader, pos: BlockPos): BlockState = makeConnections(reader, defaultState, pos)

    override fun updatePostPlacement(stateIn: BlockState, facing: Direction, facingState: BlockState, worldIn: IWorld, currentPos: BlockPos, facingPos: BlockPos): BlockState?
    {
        return if (!stateIn.isValidPosition(worldIn, currentPos))
        {
            worldIn.pendingBlockTicks.scheduleTick(currentPos, this, 1)
            super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos)
        } else
        {
            val block = facingState.block
            val isValid = block === this || block === PhoenixBlocks.KIKIN_FRUIT.get() || facing == Direction.DOWN && block === PhoenixBlocks.FERTILE_END_STONE.get()
            stateIn.with(SixWayBlock.FACING_TO_PROPERTY_MAP[facing], isValid)
        }
    }

    override fun isValidPosition(state: BlockState?, wordIn: IWorldReader, pos: BlockPos): Boolean
    {
        val flag = wordIn.isAirBlock(pos.up())
        val flag1 = wordIn.isAirBlock(pos.down())
        for (enumfacing in Direction.Plane.HORIZONTAL)
        {
            val blockpos = pos.offset(enumfacing)
            val block = wordIn.getBlockState(blockpos).block
            if (block === this)
            {
                if (!flag && !flag1)
                {
                    return false
                }
                val block1 = wordIn.getBlockState(blockpos.up()).block
                if (block1 === this || block1 === PhoenixBlocks.FERTILE_END_STONE.get())
                {
                    return true
                }
            }
        }
        val block2 = wordIn.getBlockState(pos.up()).block
        return block2 === this || block2 === PhoenixBlocks.FERTILE_END_STONE.get()
    }

    companion object
    {
        fun makeConnections(reader: IBlockReader, state: BlockState, pos: BlockPos): BlockState
        {
            val block = reader.getBlockState(pos.down()).block
            val block1 = reader.getBlockState(pos.up()).block
            val block2 = reader.getBlockState(pos.north()).block
            val block3 = reader.getBlockState(pos.east()).block
            val block4 = reader.getBlockState(pos.south()).block
            val block5 = reader.getBlockState(pos.west()).block
            return state.with(SixWayBlock.DOWN, isCompletely(block))
                    .with(SixWayBlock.UP, isCompletely(block1))
                    .with(SixWayBlock.NORTH, isCompletely(block2))
                    .with(SixWayBlock.EAST, isCompletely(block3))
                    .with(SixWayBlock.SOUTH, isCompletely(block4))
                    .with(SixWayBlock.WEST, isCompletely(block5))
        }

        fun isCompletely(block: Block): Boolean
        {
            return block === PhoenixBlocks.KIKIN_STEAM.get() || block === PhoenixBlocks.KIKIN_FRUIT.get() || block === PhoenixBlocks.FERTILE_END_STONE.get()
        }
    }
}

