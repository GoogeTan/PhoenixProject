package phoenix.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProviderSettings;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.server.ServerWorld;
import phoenix.init.PhoenixBiomes;
import phoenix.init.PhoenixConfiguration;
import phoenix.world.genlayers.*;

import java.util.List;
import java.util.Set;
import java.util.function.LongFunction;

public class NewEndBiomeProvider extends BiomeProvider
{
    private final Layer genLayer;
    private final SimplexNoiseGenerator generator;
    private final SharedSeedRandom random;
    private final World world;
    private static final Set<Biome> biomes;

    static
    {
        biomes = ImmutableSet.of(Biomes.THE_END, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS, PhoenixBiomes.UNDER.get(), PhoenixBiomes.HEARTVOID.get());
    }

    public NewEndBiomeProvider(EndBiomeProviderSettings settings, World worldIn)
    {
        super(biomes);
        this.world = worldIn;
        this.genLayer = createLayer(settings.getSeed());
        this.random = new SharedSeedRandom(settings.getSeed());
        this.random.skip(17292);
        this.generator = new SimplexNoiseGenerator(this.random);
    }

      
    @Override
    public Biome getNoiseBiome(int x, int y, int z)
    {
        return this.genLayer.func_215738_a(x, z);
    }

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

      
    @Override
    public List<Biome> getBiomesToSpawnIn()
    {
        return ImmutableList.copyOf(biomes);
    }

    public Layer createLayer(long seed)
    {
        IAreaFactory<LazyArea> iareafactory = getLayersApply((seedModifierIn) -> new LazyAreaLayerContext(25, seed, seedModifierIn));
        return new Layer(iareafactory);
    }

    public <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getLayersApply(LongFunction<C> context)
    {
        IAreaFactory<T> phoenix_biomes = (new ParentLayer(this)).apply(context.apply(1L));
        IAreaFactory<T> vanila_biomes =  (new ParentLayer(this)).apply(context.apply(1L));
        vanila_biomes = getBiomeLayer(vanila_biomes, context);

        if(StageSaveData.get((ServerWorld) world).getStage() >= 1)
        {
            phoenix_biomes = UnderLayer.INSTANCE.apply(context.apply(200L), phoenix_biomes);
            phoenix_biomes = HeartVoidLayer.INSTANCE.apply(context.apply(200L), phoenix_biomes);
        }
        for (int i = 0; i < PhoenixConfiguration.COMMON_CONFIG.BIOME_SIZE.get(); i++)
        {
            phoenix_biomes = ZoomLayer.NORMAL.apply(context.apply(200L), phoenix_biomes);
        }

        return UnificationLayer.INSTANCE.apply(context.apply(200L), phoenix_biomes, vanila_biomes);
    }

    public <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getBiomeLayer(IAreaFactory<T> parentLayer, LongFunction<C> contextFactory)
    {
        parentLayer = (new EndBiomeLayer()).apply(contextFactory.apply(200L), parentLayer);
        return parentLayer;
    }
}