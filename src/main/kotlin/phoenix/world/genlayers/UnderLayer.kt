package phoenix.world.genlayers

import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.INoiseRandom
import net.minecraft.world.gen.layer.traits.ICastleTransformer
import phoenix.init.PhxBiomes

object UnderLayer : ICastleTransformer
{
    override fun apply(context: INoiseRandom, north: Int, west: Int, south: Int, east: Int, center: Int): Int =
        if (context.random(5) == 0) Registry.BIOME.getId(PhxBiomes.UNDER) else center
}