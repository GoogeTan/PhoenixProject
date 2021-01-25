package phoenix.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.DoorHingeSide;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.IStringSerializable;
import phoenix.utils.LogManager;

import java.lang.reflect.Method;

import static net.minecraft.block.DoorBlock.HALF;
import static net.minecraft.block.DoorBlock.HINGE;

public enum FragileBlockState implements IStringSerializable
{
    END_STONE("end_stone", Blocks.END_STONE),
    DOOR("door", Blocks.DARK_OAK_DOOR)
    {
        @Override
        public void applyStates(BlockState state)
        {
            state.with(HINGE, DoorHingeSide.LEFT).with(HALF, DoubleBlockHalf.LOWER);
        }
    };

    String name;
    public Block block;
    FragileBlockState(String name, Block blockIn)
    {
        this.name = name;
        block = blockIn;
    }

    @Override
    public String getName()
    {
        return name;
    }

    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        try
        {
            Method fillStateContainer = block.getClass().getMethod("func_206840_a", builder.getClass());
            fillStateContainer.invoke(block, builder);
        } catch (Exception e)
        {
            try
            {
                Method fillStateContainer = block.getClass().getMethod("func_207184_a", builder.getClass());
                fillStateContainer.invoke(block, builder);
            }
            catch (Exception ex)
            {
                try
                {
                    Method fillStateContainer = block.getClass().getMethod("a", builder.getClass());
                    fillStateContainer.invoke(block, builder);
                }
                catch (Exception exe)
                {
                    LogManager.error(this, e);
                    LogManager.error(this, ex);
                    LogManager.error(this, exe);
                    System.exit(0);
                }
            }
        }
    }

    public void applyStates(BlockState state){}
}
