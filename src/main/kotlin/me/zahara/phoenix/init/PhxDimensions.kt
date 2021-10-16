package me.zahara.phoenix.init

import com.mojang.serialization.Lifecycle
import me.zahara.phoenix.modId
import me.zahara.phoenix.world.DecayBiomeSource
import me.zahara.phoenix.world.DecayChunkGenerator
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeZoomer
import net.minecraft.world.level.biome.FuzzyOffsetConstantColumnBiomeZoomer
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import java.util.*

object PhxDimensions
{
    val dimensionType : DimensionType = createDimensionType(
        height = 400,
        coordinateScale = 2.0,
        respawnAnchorWorks = true,
        bedWorks = false
    )

    val decayLevel: ResourceKey<Level> = ResourceKey.create(Registry.DIMENSION_REGISTRY, ResourceLocation(modId, "decay"))
    val decayDimension = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, ResourceLocation(modId, "decay"))

    fun registerDimensions
        (
            registry: MappedRegistry<LevelStem>,
            biomeRegistry: Registry<Biome>,
            seed: Long
        )
    {
        registry.register(
            decayDimension,
            LevelStem(
                { dimensionType },
                DecayChunkGenerator(DecayBiomeSource(seed, biomeRegistry), seed)
            ),
            Lifecycle.stable()
        )
    }


    // Только чтоб имена нормальные были
    private fun createDimensionType(
        fixedTime: OptionalLong = OptionalLong.empty(),
        hasSkylight: Boolean = true,
        hasCeiling: Boolean = true,
        ultraWarm: Boolean = false,
        natural: Boolean = true,
        coordinateScale: Double = 1.0,
        createDragonFight: Boolean = false,
        piglinSafe: Boolean = true,
        bedWorks: Boolean = true,
        respawnAnchorWorks: Boolean = false,
        hasRaids: Boolean = true,
        minY : Int = 0,
        height: Int = 256,
        logicalHeight: Int = height,
        biomeZoomer : BiomeZoomer = FuzzyOffsetConstantColumnBiomeZoomer.INSTANCE,
        infiniburn : ResourceLocation = BlockTags.INFINIBURN_OVERWORLD.name,
        effectsLocation : ResourceLocation = DimensionType.OVERWORLD_EFFECTS,
        ambientLight : Float = 0.0f
    ) : DimensionType = DimensionType.create(fixedTime, hasSkylight, hasCeiling, ultraWarm, natural, coordinateScale, createDragonFight, piglinSafe, bedWorks, respawnAnchorWorks, hasRaids, minY, height, logicalHeight, biomeZoomer, infiniburn, effectsLocation, ambientLight)
}