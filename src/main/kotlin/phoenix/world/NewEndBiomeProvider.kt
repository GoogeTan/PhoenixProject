package phoenix.world

import com.google.common.collect.ImmutableBiMap
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import net.minecraft.util.SharedSeedRandom
import net.minecraft.util.math.MathHelper
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.biome.provider.EndBiomeProviderSettings
import net.minecraft.world.gen.LazyAreaLayerContext
import net.minecraft.world.gen.SimplexNoiseGenerator
import net.minecraft.world.gen.feature.structure.Structure
import net.minecraft.world.gen.layer.Layer
import net.minecraft.world.gen.layer.ZoomLayer
import phoenix.init.PhxBiomes
import phoenix.init.PhxConfiguration
import phoenix.init.PhxFeatures
import phoenix.other.invoke
import phoenix.world.genlayers.*

class NewEndBiomeProvider(var settings: EndBiomeProviderSettings) : BiomeProvider(biomes)
{
    private lateinit var genLayer: Layer
    private val generator: SimplexNoiseGenerator
    private val random: SharedSeedRandom = SharedSeedRandom(settings.seed)

    init
    {
        this.random.skip(17292)
        this.generator = SimplexNoiseGenerator(this.random)
        initBiomeLayer()
    }

    fun initBiomeLayer()
    {
        this.genLayer = createLayer(settings.seed)
    }

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome = genLayer.func_215738_a(x, z)

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

    override fun getBiomesToSpawnIn() : List<Biome> = ImmutableList.copyOf(biomes)

    private fun createLayer(seed: Long): Layer
    {
        val context = { seedModifierIn: Long -> LazyAreaLayerContext(25, seed, seedModifierIn) }
        var phoenixBiomes = ParentLayer(this)(context(1L))
        val vanilaBiomes = EndBiomeLayer(context(200L), ParentLayer(this)(context(1L)))

        val stage = StageManager.stage

        if (stage >= 1)
        {
            phoenixBiomes = UnderLayer(context(200L), phoenixBiomes)
            phoenixBiomes = SmallIslandsUnderLayer(context(124L), phoenixBiomes)
            phoenixBiomes = HeartVoidLayer(context(472L), phoenixBiomes)
            phoenixBiomes = HeartVoidLayer(context(472L), phoenixBiomes)
        }

        for (i in 0..PhxConfiguration.biomeSize)
            phoenixBiomes = ZoomLayer.NORMAL(context(200L), phoenixBiomes)

        var res = UnificationLayer(context(200L), phoenixBiomes, vanilaBiomes)

        if (stage >= 2)
        {
            for (i in 0..3) res = PrimaryWetHeartVoidLayer(context(134L), res)
            for (i in 0..9) res = SecondaryWetHeartVoidLayer(context(134L), res)
        }

        return Layer(res)
    }

    companion object
    {
        private val biomes: Set<Biome> = ImmutableSet.of(Biomes.THE_END, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS, PhxBiomes.UNDER, PhxBiomes.HEARTVOID)
    }

    override fun hasStructure(structureIn: Structure<*>): Boolean = StageManager.stage >= structureToStage[structureIn] ?: 10 || super.hasStructure(structureIn)

    var structureToStage : Map<Structure<*>, Int> = ImmutableBiMap.of(PhxFeatures.REMAINS, 1)
}