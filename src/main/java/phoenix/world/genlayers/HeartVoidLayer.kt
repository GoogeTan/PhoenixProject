package phoenix.world.genlayers

import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.INoiseRandom
import net.minecraft.world.gen.layer.traits.ICastleTransformer
import phoenix.init.PhoenixBiomes

enum class HeartVoidLayer : ICastleTransformer
{
    INSTANCE;

    override fun apply(context: INoiseRandom, north: Int, west: Int, south: Int, east: Int, center: Int) = if (context.random(5) == 3) Registry.BIOME.getId(PhoenixBiomes.HEARTVOID.get()) else center
}