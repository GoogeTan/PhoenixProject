package phoenix.world.biomes;

import net.minecraft.world.biome.Biome;
import phoenix.utils.GenerationUtils;
import phoenix.world.builders.Builders;

public class HeartVoidBiome  extends Biome
{
    public HeartVoidBiome()
    {
        super(GenerationUtils.DEF.deafultSettingsForEnd(Builders.HEARTVOID, Builders.HEARTVOID_CONFIG));
    }
}
