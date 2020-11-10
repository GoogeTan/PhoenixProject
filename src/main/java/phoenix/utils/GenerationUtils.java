package phoenix.utils;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class GenerationUtils<T extends ISurfaceBuilderConfig>
{
    /*
     * Создает классический конфиг для биома в краю.
     */
    public static <T extends ISurfaceBuilderConfig> Biome.Builder deafultSettingsForEnd(SurfaceBuilder<T> surfaceBuilderIn, T surfaceBuilderConfigIn)
    {
        return (new Biome.Builder()).surfaceBuilder(surfaceBuilderIn, surfaceBuilderConfigIn)
                .precipitation(Biome.RainType.NONE)
                .category(Biome.Category.THEEND)
                .depth(0.1F)
                .scale(0.2F)
                .temperature(0.5F)
                .downfall(0.5F)
                .waterColor(4159204)
                .waterFogColor(329011)
                .parent(null);
    }
}
