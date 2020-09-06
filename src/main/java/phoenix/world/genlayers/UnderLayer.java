package phoenix.world.genlayers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import phoenix.init.PhoenixBiomes;

public enum  UnderLayer implements ICastleTransformer
{
    INSTANCE;

    @Override
    public int apply(INoiseRandom context, int north, int west, int south, int east, int center)
    {
        return context.random(5) == 0 ?  Registry.BIOME.getId(PhoenixBiomes.UNDER.get()): center;
    }
}