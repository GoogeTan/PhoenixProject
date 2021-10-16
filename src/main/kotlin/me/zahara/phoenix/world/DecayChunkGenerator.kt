package me.zahara.phoenix.world

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.zahara.phoenix.init.PhxBlocks
import net.minecraft.core.Registry
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.*
import java.util.*
import java.util.function.BiFunction
import java.util.function.Supplier

class DecayChunkGenerator(source : BiomeSource, seed : Long) : NoiseBasedChunkGenerator(source, seed, getDimSettings())
{
    override fun codec() = CODEC

    override fun withSeed(pSeed: Long): DecayChunkGenerator = DecayChunkGenerator(biomeSource,  pSeed)

    companion object
    {
        val CODEC : Codec<out DecayChunkGenerator> = RecordCodecBuilder.create { builder ->
            builder.group(
               BiomeSource.CODEC.fieldOf("biome_source").forGetter(DecayChunkGenerator::getBiomeSource),
               Codec.LONG.fieldOf("seed").forGetter(DecayChunkGenerator::seed),
            ).apply(builder, builder.stable(BiFunction(::DecayChunkGenerator)))
        }

        fun getDimSettings(): Supplier<NoiseGeneratorSettings>
        {
            val d0 = 0.9999999814507745
            return Supplier {
                NoiseGeneratorSettings(
                    StructureSettings(true),
                    NoiseSettings.create(
                        0, 256, NoiseSamplingSettings(d0, d0, 80.0, 160.0), NoiseSlideSettings(-10, 3, 0),
                        NoiseSlideSettings(-30, 0, 0), 1, 2, 1.0, -0.46875, true, true, false, false
                    ),
                    PhxBlocks.stone.defaultBlockState(),
                    Blocks.WATER.defaultBlockState(),
                    Int.MIN_VALUE,
                    -10,
                    0,
                    60,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false
                )
            }
        }
    }
}