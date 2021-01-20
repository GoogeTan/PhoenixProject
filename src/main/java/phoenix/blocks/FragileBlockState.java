package phoenix.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import phoenix.utils.LogManager;

import java.lang.reflect.Method;

public enum FragileBlockState implements IStringSerializable
{
    END_STONE("end_stone", Blocks.END_STONE)
            {
                @Override
                boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face)
                {
                    return false;
                }

                @Override
                int getLightValue(BlockState state)
                {
                    return 0;
                }
            };
    String name;
    public Block block;
    public static Method fillStateContainer = null;
    FragileBlockState(String name, Block blockIn)
    {
        this.name = name;
        block = blockIn;
    }

    public static void init()
    {
        for (Method m : Blocks.class.getDeclaredMethods())
        {
            try
            {
                m.invoke(Blocks.AIR, new StateContainer.Builder<Block, BlockState>(Blocks.AIR));
                fillStateContainer = m;
                LogManager.error("yes", "YES!!!");
                return;
            }
            catch (Exception ignored) {}
        }
    }

    @Override
    public String getName()
    {
        return name;
    }
    abstract boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face);
    abstract int getLightValue(BlockState state);

    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        if(fillStateContainer != null)
        {
            try
            {
                fillStateContainer.invoke(block, builder);
            }
            catch (Exception e)
            {
                LogManager.error(this, e);
            }
        }
    }
}
