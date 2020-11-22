package phoenix;

import net.minecraft.world.World;
import net.minecraft.world.biome.FuzzedBiomeMagnifier;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import phoenix.world.EndBiomedDimension;

import java.lang.reflect.Constructor;
import java.util.function.BiFunction;

public class InitEnd
{
    public static void init()
    {
        try
        {
            BiFunction<World, DimensionType, Dimension> function = EndBiomedDimension::new;
            for (Constructor<?> constructor : DimensionType.class.getConstructors())
            {
                DimensionType newEnd = (DimensionType) constructor.newInstance(2, "_end", "DIM1", function, false, FuzzedBiomeMagnifier.INSTANCE);
                DimensionType.THE_END = DimensionType.register("the_end", newEnd);
                return;
            }
        }catch (Exception e){e.printStackTrace();}
    }
}