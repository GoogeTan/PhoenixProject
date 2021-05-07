package phoenix.world.genlayers

import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.INoiseRandom
import net.minecraft.world.gen.layer.traits.ICastleTransformer
import phoenix.init.PhxBiomes

object PrimaryWetHeartVoidLayer : ICastleTransformer
{
    var WET_HEART_VOID_ID = Registry.BIOME.getId(PhxBiomes.WET_HEARTVOID)
    var HEART_VOID_ID = Registry.BIOME.getId(PhxBiomes.HEARTVOID)

    override fun apply(context: INoiseRandom, north: Int, west: Int, south: Int, east: Int, center: Int) =
            if (center == HEART_VOID_ID && context.random(5) == 3) WET_HEART_VOID_ID else center
}