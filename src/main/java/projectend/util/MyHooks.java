package projectend.util;

import hooklib.asm.Hook;
import hooklib.asm.HookPriority;
import hooklib.asm.ReturnCondition;
import net.minecraft.block.BlockEndRod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import projectend.init.BlocksRegister;

import java.util.BitSet;
import java.util.List;
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

    //@Hook(targetMethod= "renderModelFlat")
    public void renderQuadsFlat(BlockModelRenderer bmr, IBlockAccess blockAccessIn, IBlockState stateIn, BlockPos posIn, int brightnessIn, boolean ownBrightness, BufferBuilder buffer, List<BakedQuad> list, BitSet bitSet)
    {
        Vec3d vec3d = stateIn.getOffset(blockAccessIn, posIn);
        double d0 = (double)posIn.getX() + vec3d.x;
        double d1 = (double)posIn.getY() + vec3d.y;
        double d2 = (double)posIn.getZ() + vec3d.z;

        for (int i = 0; i < list.size(); ++i)
        {
            BakedQuad bakedquad = list.get(i);

            if (ownBrightness)
            {
                bmr.fillQuadBounds(stateIn, bakedquad.getVertexData(), bakedquad.getFace(), (float[])null, bitSet);
                BlockPos blockpos = bitSet.get(0) ? posIn.offset(bakedquad.getFace()) : posIn;
                brightnessIn = stateIn.getPackedLightmapCoords(blockAccessIn, blockpos);
            }

            buffer.addVertexData(bakedquad.getVertexData());
            if(stateIn.getBlock().equals(BlocksRegister.MIND.getDefaultState()))
                buffer.putBrightness4( 2 * brightnessIn, 2 * brightnessIn, 2 * brightnessIn, 2 * brightnessIn);
            else
                buffer.putBrightness4(brightnessIn, brightnessIn, brightnessIn, brightnessIn);

            if (bakedquad.hasTintIndex())
            {
                int k = bmr.blockColors.colorMultiplier(stateIn, blockAccessIn, posIn, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable)
                {
                    k = TextureUtil.anaglyphColor(k);
                }

                float f = (float)(k >> 16 & 255) / 255.0F;
                float f1 = (float)(k >> 8 & 255) / 255.0F;
                float f2 = (float)(k & 255) / 255.0F;
                if(bakedquad.shouldApplyDiffuseLighting())
                {
                    float diffuse = net.minecraftforge.client.model.pipeline.LightUtil.diffuseLight(bakedquad.getFace());
                    f *= diffuse;
                    f1 *= diffuse;
                    f2 *= diffuse;
                }
                buffer.putColorMultiplier(f, f1, f2, 4);
                buffer.putColorMultiplier(f, f1, f2, 3);
                buffer.putColorMultiplier(f, f1, f2, 2);
                buffer.putColorMultiplier(f, f1, f2, 1);
            }
            else if(bakedquad.shouldApplyDiffuseLighting())
            {
                float diffuse = net.minecraftforge.client.model.pipeline.LightUtil.diffuseLight(bakedquad.getFace());
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 4);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 3);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 2);
                buffer.putColorMultiplier(diffuse, diffuse, diffuse, 1);
            }

            buffer.putPosition(d0, d1, d2);
        }
    }

}
