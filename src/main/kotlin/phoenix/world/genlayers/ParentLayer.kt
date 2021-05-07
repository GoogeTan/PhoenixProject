package phoenix.world.genlayers

import net.minecraft.world.gen.INoiseRandom
import net.minecraft.world.gen.layer.traits.IAreaTransformer0
import phoenix.world.NewEndBiomeProvider

class ParentLayer(var provider: NewEndBiomeProvider) : IAreaTransformer0
{
    override fun apply(context: INoiseRandom, x: Int, z: Int): Int
    {

        val realX = x shr 2
        val realZ = z shr 2
        return if (realX.toLong() * realX.toLong() + realZ.toLong() * realZ.toLong() <= 1024L) //if dragon island
        {
            -1
        } else
        {
            val height = provider.func_222365_c(realX * 2 + 1, realZ * 2 + 1)
            when
            {
                height > 40.0f  -> 3
                height >= 0.0f  -> 2
                height < -20.0f -> 4
                else            -> 1
            }
        }
    }
}