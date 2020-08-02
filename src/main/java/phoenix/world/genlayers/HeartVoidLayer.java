package phoenix.world.genlayers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import phoenix.init.PhoenixBiomes;

public enum HeartVoidLayer implements ICastleTransformer
{
    INSTANCE;
    @Override
    public int apply(INoiseRandom context, int north, int west, int south, int east, int center)
    {
        return context.random(5) == 3 ?  Registry.BIOME.getId(PhoenixBiomes.HEARTVOID.get()): center;
    }
}