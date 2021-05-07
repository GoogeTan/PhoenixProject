package phoenix.world.genlayers

import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.INoiseRandom
import net.minecraft.world.gen.layer.traits.ICastleTransformer
import phoenix.init.PhxBiomes

object SecondaryWetHeartVoidLayer : ICastleTransformer
{
    var WET_HEART_VOID_ID = Registry.BIOME.getId(PhxBiomes.WET_HEARTVOID)
    var HEART_VOID_ID = Registry.BIOME.getId(PhxBiomes.HEARTVOID)

    override fun apply(context: INoiseRandom, north: Int, west: Int, south: Int, east: Int, center: Int)
            = if (center == HEART_VOID_ID && isCorrect(north, west, east, south)) WET_HEART_VOID_ID else center

    private fun isCorrect(a : Int, b : Int, c : Int, d : Int): Boolean
    {
        if(a == WET_HEART_VOID_ID) return true
        if(b == WET_HEART_VOID_ID) return true
        if(c == WET_HEART_VOID_ID) return true
        if(d == WET_HEART_VOID_ID) return true
        return false
    }
}