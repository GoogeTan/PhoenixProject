package phoenix.world.genlayers;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import phoenix.init.PhoenixBiomes;

import javax.annotation.Nonnull;

public enum  UnificationLayer implements IAreaTransformer2
{
    INSTANCE;
    private static final int END_BARRENS       = Registry.BIOME.getId(Biomes.END_BARRENS);
    private static final int THE_END           = Registry.BIOME.getId(Biomes.THE_END);
    private static final int SMALL_END_ISLANDS = Registry.BIOME.getId(Biomes.SMALL_END_ISLANDS);
    private static final int UNDER             = Registry.BIOME.getId(PhoenixBiomes.UNDER.get());
    private static final int HEARTVOID         = Registry.BIOME.getId(PhoenixBiomes.HEARTVOID.get());
    @Override
    public int apply(@Nonnull INoiseRandom random, IArea area1, IArea area2, int x, int z)
    {
        int phoenix = area1.getValue(x, z);
        int vanila  = area2.getValue(x, z);
        if(vanila == THE_END || vanila == SMALL_END_ISLANDS || vanila == END_BARRENS || (phoenix != UNDER && phoenix != HEARTVOID))
        {
            return vanila;
        }
        else
        {
            return phoenix;
        }
    }

    @Override
    public int getOffsetX(int x)
    {
        return x;
    }

    @Override
    public int getOffsetZ(int z)
    {
        return z;
    }
}
