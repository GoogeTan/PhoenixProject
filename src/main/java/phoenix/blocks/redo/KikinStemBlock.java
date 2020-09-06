package phoenix.blocks.redo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusPlantBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import phoenix.init.PhoenixBlocks;

import javax.annotation.Nonnull;

public class KikinStemBlock extends ChorusPlantBlock
{

    public KikinStemBlock()
    {
        super(Block.Properties.create(Material.ROCK).notSolid());
    }

    @Override
    public BlockState makeConnections(IBlockReader reader, BlockPos pos)
    {
        return makeConnections(reader, this.getDefaultState(), pos);
    }

    public static BlockState makeConnections(IBlockReader reader, BlockState state, BlockPos pos)
    {
        Block block  = reader.getBlockState(pos.down()) .getBlock();
        Block block1 = reader.getBlockState(pos.up())   .getBlock();
        Block block2 = reader.getBlockState(pos.north()).getBlock();
        Block block3 = reader.getBlockState(pos.east()) .getBlock();
        Block block4 = reader.getBlockState(pos.south()).getBlock();
        Block block5 = reader.getBlockState(pos.west()) .getBlock();
        return state.with(DOWN, isCompletply(block))
                .with(UP,   isCompletply(block1))
                .with(NORTH,isCompletply(block2))
                .with(EAST, isCompletply(block3))
                .with(SOUTH,isCompletply(block4))
                .with(WEST, isCompletply(block5));

    }

    static boolean isCompletply(@Nonnull Block block)
    {
        return block  == PhoenixBlocks.KIKIN_STEAM.get() || block  == PhoenixBlocks.KIKIN_FRUIT.get() || block == PhoenixBlocks.FERTILE_END_STONE.get();
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
    {
        if (!stateIn.isValidPosition(worldIn, currentPos)) {
            worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
            return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        } else {
            Block block = facingState.getBlock();
            boolean isValid = block == this || block == PhoenixBlocks.KIKIN_FRUIT.get() || facing == Direction.DOWN && block == PhoenixBlocks.FERTILE_END_STONE.get();
            return stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), isValid);
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader wordIn, BlockPos pos)
    {
        boolean flag = wordIn.isAirBlock(pos.up());
        boolean flag1 = wordIn.isAirBlock(pos.down());

        for (Direction enumfacing : Direction.Plane.HORIZONTAL)
        {
            BlockPos blockpos = pos.offset(enumfacing);
            Block block = wordIn.getBlockState(blockpos).getBlock();

            if (block == this)
            {
                if (!flag && !flag1)
                {
                    return false;
                }

                Block block1 = wordIn.getBlockState(blockpos.up()).getBlock();

                if (block1 == this || block1 == PhoenixBlocks.FERTILE_END_STONE.get())
                {
                    return true;
                }
            }
        }

        Block block2 = wordIn.getBlockState(pos.up()).getBlock();
        return block2 == this || block2 == PhoenixBlocks.FERTILE_END_STONE.get();
    }
}

