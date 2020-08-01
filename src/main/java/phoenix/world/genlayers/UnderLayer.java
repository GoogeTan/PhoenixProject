package phoenix.world.genlayers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import phoenix.init.PhoenixBiomes;

public enum  UnderLayer implements ICastleTransformer
{
    INSTANCE;
    private static final int END_BARRENS = Registry.BIOME.getId(Biomes.END_BARRENS);
    private static final int END_HIGHLANDS = Registry.BIOME.getId(Biomes.END_HIGHLANDS);
    private static final int END_MIDLANDS = Registry.BIOME.getId(Biomes.END_MIDLANDS);
    private static final int THE_END = Registry.BIOME.getId(Biomes.THE_END);
    private static final int SMALL_END_ISLANDS = Registry.BIOME.getId(Biomes.SMALL_END_ISLANDS);
    private static final int UNDER = Registry.BIOME.getId(PhoenixBiomes.UNDER.get());

    @Override
    public int apply(INoiseRandom context, int north, int west, int south, int east, int center)
    {
        if(center == END_BARRENS || center == END_HIGHLANDS || center == END_MIDLANDS)
        {
            System.out.println(north + " " + east + " " + west + " " + south + " айдишки " + END_HIGHLANDS + " " + END_MIDLANDS + " " + END_BARRENS+ " ундер " + UNDER);
            int sum = 0;
            if(north == UNDER)
                sum++;
            if(south == UNDER)
                sum++;
            if(west == UNDER)
                sum++;
            if(east == UNDER)
                sum++;
            if(sum >= 2) return UNDER;
            else if(sum == 1) return context.random(5) != 2 ?  UNDER: center;

            return context.random(80) == 0 ?  UNDER: center;
        }
        return center;
    }
}
