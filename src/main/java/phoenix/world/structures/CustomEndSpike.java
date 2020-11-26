package phoenix.world.structures;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import phoenix.Phoenix;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;
import java.util.function.Function;

@ParametersAreNonnullByDefault
public class CustomEndSpike extends EndSpikeFeature
{
    public static CustomEndSpike getSpike()
    {
       return Phoenix.getCustomEndSpike();
    }
    public CustomEndSpike(Function<Dynamic<?>, ? extends EndSpikeFeatureConfig> function)
    {
        super(function);
    }

    @Override
    public void placeSpike(IWorld world, Random rand, EndSpikeFeatureConfig config, EndSpike spike)
    {
        super.placeSpike(world, rand, config, spike);
        Phoenix.getLOGGER().error("EEEEEEEEEEEEEEEEEEESSSSSSSSSSSSSSSSSSSSSSSSS!!!!!!!!!!!!!!!!!!!!!!!");
        world.setBlockState(new BlockPos(spike.getCenterX(), 100, spike.getCenterX()), Blocks.ACACIA_WOOD.getDefaultState(), 2);
    }
}
