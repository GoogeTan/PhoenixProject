package phoenix.blocks.ash;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class PotteryBarrelBlock extends Block
{
    public static final BooleanProperty notEmpty = BooleanProperty.create("upper");
    public static final IntegerProperty countOfJumps = IntegerProperty.create("jumps", 0, 1000);
    public PotteryBarrelBlock()
    {
        super(Properties.create(Material.BAMBOO));
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(notEmpty, Boolean.valueOf(false)).with(countOfJumps, Integer.valueOf(0)));
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        if (pos.getY() < entityIn.getPosY())
        {
            worldIn.setBlockState(pos, state.with(countOfJumps, state.get(countOfJumps).intValue() + 1));
            try
            {
                entityIn.sendMessage(new StringTextComponent(state.get(countOfJumps).intValue() + ""));
            } catch (Exception e){}
        }

        super.onEntityCollision(state, worldIn, pos, entityIn);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(notEmpty).add(countOfJumps);
    }
}
