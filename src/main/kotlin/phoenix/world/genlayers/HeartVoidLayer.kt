package phoenix.world.genlayers

import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.INoiseRandom
import net.minecraft.world.gen.layer.traits.ICastleTransformer
import phoenix.init.PhoenixBiomes

object HeartVoidLayer : ICastleTransformer
{
    var HEART_VOID_ID = Registry.BIOME.getId(PhoenixBiomes.HEARTVOID.get());
    override fun apply(context: INoiseRandom, north: Int, west: Int, south: Int, east: Int, center: Int)
    = if (context.random(5) == 3 || isCurrect(north, west, east, south)) HEART_VOID_ID else center

    fun isCurrect(a : Int, b : Int, c : Int, d : Int): Boolean
    {
        if(a == HEART_VOID_ID && b == HEART_VOID_ID && c == HEART_VOID_ID) return true
        if(b == HEART_VOID_ID && c == HEART_VOID_ID && d == HEART_VOID_ID) return true
        if(a == HEART_VOID_ID && c == HEART_VOID_ID && d == HEART_VOID_ID) return true
        if(a == HEART_VOID_ID && b == HEART_VOID_ID && d == HEART_VOID_ID) return true
        return false
    }
}