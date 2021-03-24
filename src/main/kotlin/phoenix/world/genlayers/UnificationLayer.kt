package phoenix.world.genlayers

import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biomes
import net.minecraft.world.gen.INoiseRandom
import net.minecraft.world.gen.area.IArea
import net.minecraft.world.gen.layer.traits.IAreaTransformer2
import phoenix.init.PhoenixBiomes


object UnificationLayer : IAreaTransformer2
{
    override fun apply(random: INoiseRandom, area1: IArea, area2: IArea, x: Int, z: Int): Int
    {
        val phoenix = area1.getValue(x, z)
        val vanila = area2.getValue(x, z)
        return if (vanila == THE_END || vanila == SMALL_END_ISLANDS || vanila == END_BARRENS || phoenix != UNDER && phoenix != HEART_VOID)
        {
            vanila
        } else
        {
            phoenix
        }
    }

    override fun getOffsetX(x: Int) = x
    override fun getOffsetZ(z: Int) = z

    private val END_BARRENS: Int = Registry.BIOME.getId(Biomes.END_BARRENS)
    private val THE_END: Int = Registry.BIOME.getId(Biomes.THE_END)
    private val SMALL_END_ISLANDS: Int = Registry.BIOME.getId(Biomes.SMALL_END_ISLANDS)
    private val UNDER: Int = Registry.BIOME.getId(PhoenixBiomes.UNDER.get())
    private val HEART_VOID: Int = Registry.BIOME.getId(PhoenixBiomes.HEARTVOID.get())

}