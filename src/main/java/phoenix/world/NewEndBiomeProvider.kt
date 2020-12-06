package phoenix.world

import com.google.common.collect.ImmutableBiMap
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import com.google.common.collect.ImmutableSet
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.MathHelper
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.biome.provider.EndBiomeProviderSettings
import net.minecraft.world.gen.IExtendedNoiseRandom
import net.minecraft.world.gen.LazyAreaLayerContext
import net.minecraft.world.gen.SimplexNoiseGenerator
import net.minecraft.world.gen.area.IArea
import net.minecraft.world.gen.area.IAreaFactory
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.layer.Layer
import net.minecraft.world.gen.layer.ZoomLayer
import net.minecraft.world.server.ServerWorld
import phoenix.Phoenix
import phoenix.init.PhoenixBiomes
import phoenix.init.PhoenixConfiguration
import phoenix.init.PhoenixFeatures
import phoenix.world.genlayers.*
import java.util.function.LongFunction


class NewEndBiomeProvider(var settings: EndBiomeProviderSettings, worldIn: ServerWorld) : BiomeProvider(biomes)
{
    private lateinit var genLayer: Layer
    private val generator: SimplexNoiseGenerator
    private val random: SharedSeedRandom = SharedSeedRandom(settings.seed)
    private val world: ServerWorld = worldIn

    init
    {
        this.random.skip(17292);
        this.generator = SimplexNoiseGenerator(this.random)
        initBiomeLayer()
    }

    fun initBiomeLayer()
    {
        this.genLayer = createLayer(settings.seed, world)
    }

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome
    {
        return genLayer.func_215738_a(x, z)
    }

    override fun func_222365_c(x: Int, z: Int): Float
    {
        val realX = x / 2
        val realZ = z / 2
        val dopX = x % 2
        val dopZ = z % 2
        var result = 100.0f - MathHelper.sqrt((x * x + z * z).toFloat()) * 8.0f
        result = MathHelper.clamp(result, -100.0f, 80.0f)
        for (i in -12..12)
        {
            for (j in -12..12)
            {
                val currentX = realX + i.toLong()
                val currentZ = realZ + j.toLong()
                if (currentX * currentX + currentZ * currentZ > 4096L && this.generator.getValue(currentX.toDouble(), currentZ.toDouble()) < -0.8999999761581421)
                {
                    val lvt_14_1_ = (MathHelper.abs(currentZ.toFloat()) * 3439.0f + MathHelper.abs(currentZ.toFloat()) * 147.0f) % 13.0f + 9.0f
                    val lvt_15_1_ = (dopX - i * 2).toFloat()
                    val lvt_16_1_ = (dopZ - j * 2).toFloat()
                    var lvt_17_1_ = 100.0f - MathHelper.sqrt(lvt_15_1_ * lvt_15_1_ + lvt_16_1_ * lvt_16_1_) * lvt_14_1_
                    lvt_17_1_ = MathHelper.clamp(lvt_17_1_, -100.0f, 80.0f)
                    result = result.coerceAtLeast(lvt_17_1_)
                }
            }
        }
        return result
    }

    override fun getBiomesToSpawnIn() = ImmutableList.copyOf(Companion.biomes)

    private fun createLayer(seed: Long, world: ServerWorld?): Layer
    {
        val factory = getLayersApply(world) { seedModifierIn: Long -> LazyAreaLayerContext(25, seed, seedModifierIn) }
        return Layer(factory)
    }

    private fun <T : IArea?, C : IExtendedNoiseRandom<T>> getLayersApply(worldIn: ServerWorld?, context: LongFunction<C>): IAreaFactory<T>
    {
        var phoenixBiomes = ParentLayer(this).apply(context.apply(1L))
        var vanilaBiomes = ParentLayer(this).apply(context.apply(1L))
        vanilaBiomes = getBiomeLayer(vanilaBiomes, context)
        var stage = 2
        try
        {
            stage = StageManager.getStage()
        } catch (ignored: Exception)
        {
            ignored.printStackTrace()
        }
        Phoenix.LOGGER.error(this.javaClass.toString() + " " +  stage)
        if (stage >= 1)
        {
            phoenixBiomes = UnderLayer.INSTANCE.apply(context.apply(200L), phoenixBiomes)
            phoenixBiomes = HeartVoidLayer.INSTANCE.apply(context.apply(200L), phoenixBiomes)
        }

        for (i in 0..PhoenixConfiguration.COMMON_CONFIG.BIOME_SIZE.get())
        {
            phoenixBiomes = ZoomLayer.NORMAL.apply(context.apply(200L), phoenixBiomes)
        }
        return UnificationLayer.INSTANCE.apply(context.apply(200L), phoenixBiomes, vanilaBiomes)
    }

    private fun <T : IArea?, C : IExtendedNoiseRandom<T>> getBiomeLayer(parentLayer: IAreaFactory<T>, contextFactory: LongFunction<C>): IAreaFactory<T>
    {
        var parentLayer = parentLayer
        parentLayer = EndBiomeLayer().apply(contextFactory.apply(200L), parentLayer)
        return parentLayer
    }

    companion object
    {
        private val biomes: Set<Biome> = ImmutableSet.of(Biomes.THE_END, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS, PhoenixBiomes.UNDER.get(), PhoenixBiomes.HEARTVOID.get())
    }

    override fun hasStructure(structureIn: Structure<*>): Boolean
    {
        return if(structureToStage.containsKey(structureIn))
        {
            StageManager.getStage() >= structureToStage[structureIn] ?: 10
        }
        else
        {
            super.hasStructure(structureIn)
        }
    }

    var structureToStage : Map<Structure<*>, Int> = ImmutableBiMap.of(PhoenixFeatures.REMAINS.get(), 1)
}