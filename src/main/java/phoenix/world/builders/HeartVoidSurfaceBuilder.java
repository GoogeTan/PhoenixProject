package phoenix.world.builders;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import phoenix.init.PhoenixBlocks;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Function;

public class HeartVoidSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig>
{

    public HeartVoidSurfaceBuilder(Function<Dynamic<?>, ? extends SurfaceBuilderConfig> function)
    {
        super(function);
    }

    @Override
    public void buildSurface(Random random, @Nonnull IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise,
                             @Nonnull BlockState defaultBlock, @Nonnull BlockState defaultFluid, int seaLevel, long seed, @Nonnull SurfaceBuilderConfig config)
    {
        BlockState top_block = config.getTop();
        BlockState middle_block = config.getUnder();
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
                if(!(isAir(chunkIn, currect_pos.up(9))    || isAir(chunkIn, currect_pos.down(9))  ||
                     isAir(chunkIn, currect_pos.south(9)) || isAir(chunkIn, currect_pos.north(9)) ||
                     isAir(chunkIn, currect_pos.west(9))  || isAir(chunkIn, currect_pos.east(9))))
                {
                    if(currect_state.getBlock() == Blocks.END_STONE)
                    {
                        chunkIn.setBlockState(currect_pos, PhoenixBlocks.ANTI_AIR.get().getDefaultState(), false);
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

    static boolean isAir(IChunk chunk, BlockPos pos)
    {
        return isAir(chunk.getBlockState(pos));
    }
    static boolean isAir(BlockState state)
    {
        return state.isAir() && state.getBlock() != PhoenixBlocks.ANTI_AIR.get();
    }
}