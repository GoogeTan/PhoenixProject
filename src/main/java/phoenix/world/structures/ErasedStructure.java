package phoenix.world.structures;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.EndCityPieces;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import phoenix.init.PhoenixConfiguration;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.Function;

public class ErasedStructure extends Structure<NoFeatureConfig>
{
    public ErasedStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
    {
        super(configFactoryIn);
    }

    @Override
    public boolean canBeGenerated(@Nonnull BiomeManager biomeManagerIn, @Nonnull ChunkGenerator generatorIn, @Nonnull Random randIn, int chunkX, int chunkZ, Biome biomeIn)
    {
        return canBeGeneratedOld(biomeManagerIn, generatorIn, randIn, chunkX, chunkZ, biomeIn) && PhoenixConfiguration.COMMON_CONFIG.GENERATE_OPTIONAL_STRUCTURES.get();
    }


    public boolean canBeGeneratedOld(BiomeManager biomeManagerIn, ChunkGenerator<?> generatorIn, Random randIn, int chunkX, int chunkZ, Biome biomeIn)
    {
        ChunkPos chunkpos = this.getStartPositionForPosition(generatorIn, randIn, chunkX, chunkZ, 0, 0);
        if (chunkX == chunkpos.x && chunkZ == chunkpos.z)
        {
            if (!generatorIn.hasStructure(biomeIn, this))
            {
                return false;
            }
            else
            {
                int i = getYPosForStructure(chunkX, chunkZ, generatorIn);
                return i >= 60;
            }
        }
        else
        {
            return false;
        }
    }

    @Nonnull
    @Override
    public IStartFactory getStartFactory()
    {
        return Start::new;
    }

    @Nonnull
    @Override
    public String getStructureName()
    {
        return "erasedcity";
    }

    @Override
    public int getSize()
    {
        return 15;
    }

    private static int getYPosForStructure(int chunkX, int chunkY, ChunkGenerator<?> generatorIn)
    {
        Random random = new Random(chunkX + chunkY * 10387313);
        Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        int i = 5;
        int j = 5;
        if (rotation == Rotation.CLOCKWISE_90)
        {
            i = -5;
        }
        else if (rotation == Rotation.CLOCKWISE_180)
        {
            i = -5;
            j = -5;
        }
        else if (rotation == Rotation.COUNTERCLOCKWISE_90)
        {
            j = -5;
        }

        int k = (chunkX << 4) + 7;
        int l = (chunkY << 4) + 7;
        int i1 = generatorIn.getNoiseHeightMinusOne(k, l, Heightmap.Type.WORLD_SURFACE_WG);
        int j1 = generatorIn.getNoiseHeightMinusOne(k, l + j, Heightmap.Type.WORLD_SURFACE_WG);
        int k1 = generatorIn.getNoiseHeightMinusOne(k + i, l, Heightmap.Type.WORLD_SURFACE_WG);
        int l1 = generatorIn.getNoiseHeightMinusOne(k + i, l + j, Heightmap.Type.WORLD_SURFACE_WG);
        return Math.min(Math.min(i1, j1), Math.min(k1, l1));
    }

    public static class Start extends StructureStart
    {
        public Start(Structure<?> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox mbb, int references, long seed)
        {
            super(structure, chunkPosX, chunkPosZ, mbb, references, seed);
        }

        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn)
        {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            int i = ErasedStructure.getYPosForStructure(chunkX, chunkZ, generator);
            if (i >= 40)
            {
                BlockPos blockpos = new BlockPos(chunkX * 16 + 8, i, chunkZ * 16 + 8);
                EndCityPieces.startHouseTower(templateManagerIn, blockpos, rotation, this.components, this.rand);
                this.recalculateStructureSize();
            }
        }
    }
}