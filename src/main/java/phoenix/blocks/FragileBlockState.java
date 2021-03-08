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
    END_STONE("end_stone", Blocks.END_STONE);

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
    }

    public void applyStates(BlockState state){}
}
