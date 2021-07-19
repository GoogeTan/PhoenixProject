package phoenix.utils

import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder

object GenerationUtils
{
    /*
     * Создает стандратный конфиг для биома в краю.
     */
    fun <T : ISurfaceBuilderConfig> defaultSettingsForEnd(surfaceBuilderIn: SurfaceBuilder<T>, surfaceBuilderConfigIn: T): Biome.Builder
    {
        return Biome.Builder().surfaceBuilder(surfaceBuilderIn, surfaceBuilderConfigIn)
            .precipitation(Biome.RainType.NONE)
            .category(Biome.Category.THEEND)
            .depth(0.1f)
            .scale(0.2f)
            .temperature(0.5f)
            .downfall(0.5f)
            .waterColor(4159204)
            .waterFogColor(329011)
            .parent(null)
    }
}