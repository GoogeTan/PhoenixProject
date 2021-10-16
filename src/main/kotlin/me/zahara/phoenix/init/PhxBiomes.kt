package me.zahara.phoenix.init

import me.zahara.phoenix.biome.BiomeSurfaceConfig
import me.zahara.phoenix.init.registery.AutoRegisterer
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.level.biome.*

object  PhxBiomes : AutoRegisterer<Biome>(Biome::class.java)
{
    val valley : Biome by register("valley") {
        makeBiome(
            precipitation = Biome.Precipitation.RAIN,
            biomeCategory = Biome.BiomeCategory.EXTREME_HILLS,
            config = BiomeSurfaceConfig(1.0f, 1f, 0.999f),
            temperature = 0.7f,
            ambient = BiomeSpecialEffects.Builder().waterColor(0x00354D).fogColor(0x283639).grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE).ambientMoodSound(AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0)).build(),
            mobSpawnSettings = MobSpawnSettings.Builder().build(),
            generationSettings = BiomeGenerationSettings.Builder().surfaceBuilder(PhxSurfaceBuilders.defaultConfigured).build()
        )
    }

    val mountains : Biome by register("mountains") {
        makeBiome(
            precipitation = Biome.Precipitation.RAIN,
            biomeCategory = Biome.BiomeCategory.EXTREME_HILLS,
            config = BiomeSurfaceConfig(1.8f, 2f, 1.7f),
            temperature = 0.7f,
            ambient = BiomeSpecialEffects.Builder().waterColor(0x00354D).fogColor(0x283639).grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE).ambientMoodSound(AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0)).build(),
            mobSpawnSettings = MobSpawnSettings.Builder().build(),
            generationSettings = BiomeGenerationSettings.Builder().surfaceBuilder(PhxSurfaceBuilders.defaultConfigured).build()
        )
    }

    val sea : Biome by register("sea") {
        makeBiome(
            generationSettings = BiomeGenerationSettings.Builder().surfaceBuilder(PhxSurfaceBuilders.defaultConfigured).build(),
            config = BiomeSurfaceConfig(0.5f, 1.0f, 0.5f),
            temperature = 0.4f,
            ambient = BiomeSpecialEffects.Builder().waterColor(0x00354D).fogColor(0x283639).grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE).ambientMoodSound(AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0)).build(),
        )
    }

    private fun makeBiome(
        precipitation: Biome.Precipitation = Biome.Precipitation.NONE,
        biomeCategory: Biome.BiomeCategory = Biome.BiomeCategory.NONE,
        config : BiomeSurfaceConfig,
        temperature: Float = 1.0f,
        ambient : BiomeSpecialEffects,
        mobSpawnSettings: MobSpawnSettings = MobSpawnSettings.Builder().build(),
        generationSettings : BiomeGenerationSettings
    ): Biome = Biome.BiomeBuilder()
        .precipitation(precipitation)
        .biomeCategory(biomeCategory)
        .depth(config.depth)
        .scale(config.scale)
        .downfall(config.downfall)
        .temperature(temperature)
        .specialEffects(ambient)
        .mobSpawnSettings(mobSpawnSettings)
        .generationSettings(generationSettings).build()

    private fun calculateSkyColor(pTemperature: Float): Int
    {
        var f = pTemperature / 3.0f
        f = Mth.clamp(f, -1.0f, 1.0f)
        return Mth.hsvToRgb(0.62222224f - f * 0.05f, 0.5f + f * 0.1f, 1.0f)
    }
}