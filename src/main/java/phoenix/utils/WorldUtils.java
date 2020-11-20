package phoenix.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class WorldUtils
{
    public static boolean destroyBlock(World world, BlockPos pos, boolean shouldDrop, @Nullable Entity entity, ItemStack stack)
    {
        BlockState blockstate = world.getBlockState(pos);
        if (blockstate.isAir(world, pos))
        {
            return false;
        }
        else
        {
            IFluidState ifluidstate = world.getFluidState(pos);
            world.playEvent(2001, pos, Block.getStateId(blockstate));
            if (shouldDrop)
            {
                TileEntity tileentity = blockstate.hasTileEntity() ? world.getTileEntity(pos) : null;
                Block.spawnDrops(blockstate, world, pos, tileentity, entity, stack);
            }

            return world.setBlockState(pos, ifluidstate.getBlockState(), 3);
        }
    }
}
