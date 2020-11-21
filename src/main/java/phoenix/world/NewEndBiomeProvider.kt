package phoenix.world

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableSet
import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biomes
import net.minecraft.world.biome.provider.BiomeProvider
import net.minecraft.world.biome.provider.EndBiomeProviderSettings
import net.minecraft.world.gen.IExtendedNoiseRandom
import net.minecraft.world.gen.LazyAreaLayerContext
import net.minecraft.world.gen.SimplexNoiseGenerator
import net.minecraft.world.gen.area.IArea
import net.minecraft.world.gen.area.IAreaFactory
import net.minecraft.world.gen.layer.Layer
import net.minecraft.world.gen.layer.ZoomLayer
import net.minecraft.world.server.ServerWorld
import phoenix.init.PhoenixBiomes
import phoenix.init.PhoenixConfiguration
import phoenix.world.genlayers.*
import java.util.function.LongFunction
import javax.annotation.Nonnull


class NewEndBiomeProvider(settings: EndBiomeProviderSettings, @Nonnull worldIn: ServerWorld?) : BiomeProvider(biomes)
{
    private val genLayer: Layer

    companion object
    {
        private val biomes: Set<Biome> = ImmutableSet.of(Biomes.THE_END, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS, PhoenixBiomes.UNDER.get(), PhoenixBiomes.HEARTVOID.get())
    }

    override fun getNoiseBiome(x: Int, y: Int, z: Int): Biome
    {
        return genLayer.func_215738_a(x, z)
    }

    /*
    @Override
    public float func_222365_c(int x, int z)
    {
        int real_x = x / 2;
        int real_Z = z / 2;
        int dop_x = x % 2;
        int dop_z = z % 2;
        float result = 100.0F - MathHelper.sqrt((float) (x * x + z * z)) * 8.0F;
        result = MathHelper.clamp(result, -100.0F, 80.0F);

        for (int i = -12; i <= 12; ++i)
        {
            for (int j = -12; j <= 12; ++j)
            {
                long currect_x = real_x + i;
                long currect_z = real_Z + j;
                if (currect_x * currect_x + currect_z * currect_z > 4096L && this.generator.getValue((double) currect_x, (double) currect_z) < -0.8999999761581421D)
                {
                    float lvt_14_1_ = (MathHelper.abs((float) currect_z) * 3439.0F + MathHelper.abs((float) currect_z) * 147.0F) % 13.0F + 9.0F;
                    float lvt_15_1_ = (float) (dop_x - i * 2);
                    float lvt_16_1_ = (float) (dop_z - j * 2);
                    float lvt_17_1_ = 100.0F - MathHelper.sqrt(lvt_15_1_ * lvt_15_1_ + lvt_16_1_ * lvt_16_1_) * lvt_14_1_;
                    lvt_17_1_ = MathHelper.clamp(lvt_17_1_, -100.0F, 80.0F);
                    result = Math.max(result, lvt_17_1_);
                }
            }
        }

        return result;
    }
    // */
    override fun getBiomesToSpawnIn(): List<Biome>
    {
        return ImmutableList.copyOf(Companion.biomes)
    }

    fun createLayer(seed: Long, world: ServerWorld?): Layer
    {
        val factory = getLayersApply(world) { seedModifierIn: Long -> LazyAreaLayerContext(25, seed, seedModifierIn) }
        return Layer(factory)
    }

    fun <T : IArea?, C : IExtendedNoiseRandom<T>?> getLayersApply(worldIn: ServerWorld?, context: LongFunction<C>): IAreaFactory<T>
    {
        var phoenix_biomes = ParentLayer(this).apply(context.apply(1L))
        var vanila_biomes = ParentLayer(this).apply(context.apply(1L))
        vanila_biomes = getBiomeLayer(vanila_biomes, context)
        var stage = 0
        try
        {
            stage = StageSaveData.get(worldIn!!).stage
        } catch (ignored: Exception)
        {
        }
        if (stage >= 1)
        {
            phoenix_biomes = UnderLayer.INSTANCE.apply(context.apply(200L), phoenix_biomes)
            phoenix_biomes = HeartVoidLayer.INSTANCE.apply(context.apply(200L), phoenix_biomes)
        }
        for (i in 0..PhoenixConfiguration.COMMON_CONFIG.BIOME_SIZE.get())
        {
            phoenix_biomes = ZoomLayer.NORMAL.apply(context.apply(200L), phoenix_biomes)
        }
        return UnificationLayer.INSTANCE.apply(context.apply(200L), phoenix_biomes, vanila_biomes)
    }

    fun <T : IArea?, C : IExtendedNoiseRandom<T>?> getBiomeLayer(parentLayer: IAreaFactory<T>, contextFactory: LongFunction<C>): IAreaFactory<T>
    {
        var parentLayer = parentLayer
        parentLayer = EndBiomeLayer().apply(contextFactory.apply(200L), parentLayer)
        return parentLayer
    }

    init
    {
        genLayer = createLayer(settings.seed, worldIn)
        val random = SharedSeedRandom(settings.seed)
        random.skip(17292)
        SimplexNoiseGenerator(random)
    }
}