package phoenix.world.builders;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.DefaultSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import phoenix.init.PhoenixBlocks;

import java.util.Random;
import java.util.function.Function;

public class UnderSurfaceBuilder extends SurfaceBuilder<AdvansedSurfaceBuilderConfig>
{

    public UnderSurfaceBuilder(Function<Dynamic<?>, ? extends AdvansedSurfaceBuilderConfig> function)
    {
        super(function);
    }


    @Override
    public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise,
                             BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, AdvansedSurfaceBuilderConfig config)
    {
        this.buildSurface(random, chunkIn, x, z, startHeight, noise, defaultBlock, config.getTop(), config.getUnder(), config.getAdvanced());
    }

    protected void buildSurface(Random random, IChunk chunkIn, int x, int z, int startHeight, double noise,
                                BlockState defaultBlock, BlockState top, BlockState middle, BlockState under)
    {
        BlockState top_block = top;
        BlockState middle_block = middle;
        BlockPos.Mutable currect_pos = new BlockPos.Mutable();
        int i = -1;
        int noise_height = (int) (noise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
        int currect_x = x & 15;
        int currect_z = z & 15;

        for (int y = startHeight; y >= 0; --y)
        {
            currect_pos.setPos(currect_x, y, currect_z);
            BlockState currect_state = chunkIn.getBlockState(currect_pos);
            if (currect_state.isAir())
            {
                i = -1;
            }
            else if (currect_state.getBlock() == defaultBlock.getBlock())
            {
                if(y < 30 + random.nextInt(5))
                {
                    if(currect_state.getBlock() == Blocks.END_STONE)
                    {
                        chunkIn.setBlockState(currect_pos, under, false);
                    }
                }
                else if (i == -1)
                {
                    if (noise_height <= 0)
                    {
                        top_block = Blocks.AIR.getDefaultState();
                        middle_block = defaultBlock;
                    }

                    i = noise_height;
                    chunkIn.setBlockState(currect_pos, top_block, false);
                }
                else if (i > 0)
                {
                    --i;
                    chunkIn.setBlockState(currect_pos, middle_block, false);
                }
            }
        }
    }
}
