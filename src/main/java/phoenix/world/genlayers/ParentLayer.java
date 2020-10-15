package phoenix.world.genlayers;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IAreaTransformer0;
import phoenix.world.NewEndBiomeProvider;

public class ParentLayer implements IAreaTransformer0
{
    NewEndBiomeProvider provider;
    public ParentLayer(NewEndBiomeProvider providerIn)
    {
        provider = providerIn;
    }

    public int apply(int x, int z, NewEndBiomeProvider provider)
    {
        int res;
        int real_x = x >> 2;
        int real_z = z >> 2;
        if ((long)real_x * (long)real_x + (long)real_z * (long)real_z <= 1024L)//if dragon island
        {
            res = -1;
        }
        else
        {
            float height = provider.func_222365_c(real_x * 2 + 1, real_z * 2 + 1);
            if (height > 40.0F)
            {
                res = 3;
            }
            else if (height >= 0.0F)
            {
                res = 2;
            }
            else
            {
                res = height < -20.0F ? 4 : 1;
            }
        }
        return res;
    }

    @Override
    public int apply(   INoiseRandom context, int x, int z)
    {
        return apply(x, z, provider);
    }
}