package projectend.util;

import hooklib.asm.Hook;
import hooklib.asm.HookPriority;
import hooklib.asm.ReturnCondition;
import net.minecraft.block.BlockEndRod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

import static net.minecraft.block.BlockDirectional.FACING;

public class MyHooks
{
    @SideOnly(Side.CLIENT)
    @Hook(targetMethod= "randomDisplayTick", returnCondition = ReturnCondition.ALWAYS, priority = HookPriority.HIGH)
    public static void randomDisplayTick(BlockEndRod ber, IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
        double d0 = (double)pos.getX() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d1 = (double)pos.getY() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d2 = (double)pos.getZ() + 0.55D - (double)(rand.nextFloat() * 0.1F);
        double d3 = (double)(0.4F   - (rand.nextFloat() + rand.nextFloat())* 0.4F);
        if(worldIn.getBlockState(pos.down().down()).getBlock() == Blocks.END_ROD)
            worldIn.spawnParticle(EnumParticleTypes.END_ROD, d0 + (double) enumfacing.getXOffset() * d3, d1 + (double) enumfacing.getYOffset() * d3, d2 + (double) enumfacing.getZOffset() * d3,
                    rand.nextGaussian() * 0.01D,  rand.nextGaussian() * -0.005D, rand.nextGaussian() * 0.01D, 216, 191, 216, 600 + rand.nextInt(300));
        else if (rand.nextInt(5) == 0)
            worldIn.spawnParticle(EnumParticleTypes.END_ROD, d0 + (double) enumfacing.getXOffset() * d3, d1 + (double)enumfacing.getYOffset() * d3, d2 + (double)enumfacing.getZOffset() * d3,
                    rand.nextGaussian() * 0.005D, rand.nextGaussian() *  0.005D, rand.nextGaussian() * 0.005D);
    }
}
