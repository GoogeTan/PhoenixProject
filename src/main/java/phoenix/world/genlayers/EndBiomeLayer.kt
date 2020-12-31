package phoenix.world.genlayers

import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biomes
import net.minecraft.world.gen.INoiseRandom
import net.minecraft.world.gen.layer.traits.IC0Transformer
import org.apache.logging.log4j.LogManager

object EndBiomeLayer : IC0Transformer
{
    override fun apply(context: INoiseRandom, value: Int): Int
    {
        return when (value)
        {
            1    -> END_BARRENS
            2    -> END_MIDLANDS
            3    -> END_HIGHLANDS
            4    -> SMALL_END_ISLANDS
            else -> THE_END
        }
    }

    private val LOGGER = LogManager.getLogger()
    private val END_BARRENS = Registry.BIOME.getId(Biomes.END_BARRENS)
    private val END_HIGHLANDS = Registry.BIOME.getId(Biomes.END_HIGHLANDS)
    private val END_MIDLANDS = Registry.BIOME.getId(Biomes.END_MIDLANDS)
    private val THE_END = Registry.BIOME.getId(Biomes.THE_END)
    private val SMALL_END_ISLANDS = Registry.BIOME.getId(Biomes.SMALL_END_ISLANDS)

}
