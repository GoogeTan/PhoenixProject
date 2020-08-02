package phoenix.world.genlayers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phoenix.init.PhoenixBiomes;

import javax.annotation.Nonnull;

public class EndBiomeLayer  implements IC0Transformer
{
    private static final Logger LOGGER = LogManager.getLogger();

    private static final int END_BARRENS = Registry.BIOME.getId(Biomes.END_BARRENS);
    private static final int END_HIGHLANDS = Registry.BIOME.getId(Biomes.END_HIGHLANDS);
    private static final int END_MIDLANDS = Registry.BIOME.getId(Biomes.END_MIDLANDS);
    private static final int THE_END = Registry.BIOME.getId(Biomes.THE_END);
    private static final int SMALL_END_ISLANDS = Registry.BIOME.getId(Biomes.SMALL_END_ISLANDS);

    @Override
    public int apply(@Nonnull INoiseRandom context, int value)
    {
        switch (value)
        {
            case 1:  return  END_BARRENS;
            case 2:  return  END_MIDLANDS;
            case 3:  return  END_HIGHLANDS;
            case 4:  return  SMALL_END_ISLANDS;
            default: return THE_END;
        }
    }
}
