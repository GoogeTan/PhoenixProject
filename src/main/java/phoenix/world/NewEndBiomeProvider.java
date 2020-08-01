package phoenix.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProviderSettings;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.SimplexNoiseGenerator;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.feature.structure.EndCityStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.IslandLayer;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.ZoomLayer;
import net.minecraft.world.server.ServerWorld;
import phoenix.init.PhoenixBiomes;
import phoenix.utils.GenerationUtils;
import phoenix.world.genlayers.EndBiomeLayer;
import phoenix.world.genlayers.ParentLayer;
import phoenix.world.genlayers.UnderLayer;
import phoenix.world.structures.ErasedStructure;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.LongFunction;

public class NewEndBiomeProvider extends BiomeProvider
{
    private final Layer genBiomes;
    private final SimplexNoiseGenerator generator;
    private final SharedSeedRandom random;
    private World world;
    private static final Set<Biome> biomes;

    static
    {
        biomes = ImmutableSet.of(Biomes.THE_END, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS, PhoenixBiomes.UNDER.get());
    }

    public NewEndBiomeProvider(EndBiomeProviderSettings settings, World worldIn)
    {
        super(biomes);
        this.genBiomes = createLayer(settings.getSeed());
        this.random = new SharedSeedRandom(settings.getSeed());
        this.random.skip(17292);
        this.generator = new SimplexNoiseGenerator(this.random);
        this.world = worldIn;
    }

    @Override
    public Biome getNoiseBiome(int x, int y, int z)
    {
        return this.genBiomes.func_215738_a(x, z);
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
        return ImmutableList.of(Biomes.THE_END, Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, Biomes.SMALL_END_ISLANDS, Biomes.END_BARRENS, PhoenixBiomes.UNDER.get());
    }

    public Layer createLayer(long seed)
    {
        IAreaFactory<LazyArea> iareafactory = getLayersAply((seedModifierIn) -> new LazyAreaLayerContext(25, seed, seedModifierIn));
        return new Layer(iareafactory);
    }

    public <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getLayersAply(LongFunction<C> function)
    {
        IAreaFactory<T> parent = (new ParentLayer(this)).apply(function.apply(1L));
        parent = getBiomeLayer(parent, function);
        //if(world != null && !world.isRemote)
        //{
        //   if(StageSaveData.get((ServerWorld) world).getStage() >= 2)
        parent = UnderLayer.INSTANCE.apply(function.apply(200L), parent);
        //}

        return parent;
    }

    public <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getBiomeLayer(IAreaFactory<T> parentLayer, LongFunction<C> contextFactory)
    {
        parentLayer = (new EndBiomeLayer()).apply(contextFactory.apply(200L), parentLayer);
        return parentLayer;
    }


    @Override
    public boolean hasStructure(@Nonnull Structure<?> structureIn)
    {
        return super.hasStructure(structureIn) || structureIn instanceof ErasedStructure;
    }
}