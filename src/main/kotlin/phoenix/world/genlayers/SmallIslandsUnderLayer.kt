package phoenix.world.genlayers

import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.INoiseRandom
import net.minecraft.world.gen.layer.traits.ICastleTransformer
import phoenix.init.PhxBiomes

object SmallIslandsUnderLayer : ICastleTransformer
{
    override fun apply(context: INoiseRandom, north: Int, west: Int, south: Int, east: Int, center: Int): Int =
        if (context.random(5) == 0) Registry.BIOME.getId(PhxBiomes.SMALL_ISLANDS_UNDER) else center
}