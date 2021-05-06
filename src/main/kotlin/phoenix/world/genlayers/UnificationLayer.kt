package phoenix.world.genlayers

import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biomes
import net.minecraft.world.gen.INoiseRandom
import net.minecraft.world.gen.area.IArea
import net.minecraft.world.gen.layer.traits.IAreaTransformer2
import phoenix.init.PhxBiomes


object UnificationLayer : IAreaTransformer2
{
    override fun apply(random: INoiseRandom, area1: IArea, area2: IArea, x: Int, z: Int): Int
    {
        val phoenix = area1.getValue(x, z)
        val vanila = area2.getValue(x, z)
        return when
        {
            vanila == END_HIGHLANDS && (phoenix == UNDER || phoenix == HEART_VOID) -> phoenix
            vanila == SMALL_END_ISLANDS && phoenix == SMALL_ISLANDS_UNDER -> phoenix
            else -> vanila
        }
    }

    override fun getOffsetX(x: Int) = x
    override fun getOffsetZ(z: Int) = z

    private val END_BARRENS: Int = Registry.BIOME.getId(Biomes.END_BARRENS)
    private val THE_END: Int = Registry.BIOME.getId(Biomes.THE_END)
    private val SMALL_END_ISLANDS: Int = Registry.BIOME.getId(Biomes.SMALL_END_ISLANDS)
    private val END_HIGHLANDS : Int = Registry.BIOME.getId(Biomes.END_HIGHLANDS)

    private val UNDER: Int = Registry.BIOME.getId(PhxBiomes.UNDER)
    private val SMALL_ISLANDS_UNDER: Int = Registry.BIOME.getId(PhxBiomes.SMALL_ISLANDS_UNDER)
    private val HEART_VOID: Int = Registry.BIOME.getId(PhxBiomes.HEARTVOID)
}