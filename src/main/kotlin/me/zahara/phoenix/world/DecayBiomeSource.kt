package me.zahara.phoenix.world

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import me.zahara.phoenix.biome.InitLayer
import me.zahara.phoenix.biome.MountainsLayer
import me.zahara.phoenix.biome.SeaLayer
import me.zahara.phoenix.biome.ValleyLayer
import me.zahara.phoenix.init.PhxBiomes
import net.minecraft.core.Registry
import net.minecraft.resources.RegistryLookupCodec
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.newbiome.context.LazyAreaContext
import net.minecraft.world.level.newbiome.layer.Layer
import net.minecraft.world.level.newbiome.layer.Layers
import net.minecraft.world.level.newbiome.layer.ZoomLayer
import java.util.function.BiFunction
import java.util.function.Supplier
import java.util.stream.Stream

class DecayBiomeSource
    (
        private val seed: Long,
        private val biomes: Registry<Biome>
    ) : BiomeSource(Stream.of( Supplier { PhxBiomes.valley }))
{
    private val noiseBiomeLayer: Layer = getLayer()

    private fun getLayer(): Layer
    {
        var res = InitLayer.run(context(1L, seed))
        res = ZoomLayer.NORMAL.run(context(100L, seed), res)
        res = SeaLayer.run(context(100L, seed), res)
        res = ZoomLayer.NORMAL.run(context(100L, seed), res)
        res = ValleyLayer.run(context(1L, seed), res)
        res = ZoomLayer.NORMAL.run(context(100L, seed), res)
        res = ZoomLayer.NORMAL.run(context(100L, seed), res)
        res = MountainsLayer.run(context(1L, seed), res)
        res = ZoomLayer.NORMAL.run(context(100L, seed), res)
        return Layer(res)
    }

    override fun codec(): Codec<out BiomeSource> = CODEC

    override fun withSeed(pSeed: Long): BiomeSource = DecayBiomeSource(pSeed,  biomes)

    override fun getNoiseBiome(pX: Int, pY: Int, pZ: Int): Biome = noiseBiomeLayer[biomes, pX, pZ]

    companion object
    {
        val CODEC: Codec<DecayBiomeSource> = RecordCodecBuilder.create { builder: RecordCodecBuilder.Instance<DecayBiomeSource> ->
            builder.group(
                Codec.LONG.fieldOf("seed").stable().forGetter(DecayBiomeSource::seed),
                RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(DecayBiomeSource::biomes)
            ).apply(builder, builder.stable(BiFunction { pSeed: Long, pBiomes: Registry<Biome> -> DecayBiomeSource(pSeed, pBiomes) }))
        }

        private fun context(seed1: Long, seed2: Long) = LazyAreaContext(25, seed2, seed1)
    }
}