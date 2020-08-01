package phoenix.world.structures;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Random;

public class ErasedStructure extends ScatteredStructure<NoFeatureConfig>
{
    public ErasedStructure()
    {
        super(NoFeatureConfig::deserialize);
    }

    @Nonnull
    @Override
    public String getStructureName()
    {
        return "ErasedLand";
    }

    @Override
    public int getSize()
    {
        return 3;
    }

    @Nonnull
    @Override
    public Structure.IStartFactory getStartFactory()
    {
        return ErasedStructure.Start::new;
    }

    @Override
    public boolean canBeGenerated(BiomeManager biomeManagerIn, ChunkGenerator<?> generatorIn, Random randIn, int chunkX, int chunkZ, Biome biomeIn) {
        return true;
    }

    @Override
    protected int getSeedModifier()
    {
        return 14357618;
    }

    public static class Start extends StructureStart
    {
        public Start(Structure<?> str, int chunkPosX, int chunkPosZ, MutableBoundingBox bounds, int references, long seed)
        {
            super(str, chunkPosX, chunkPosZ, bounds, references, seed);
        }

        @Override
        public void init(ChunkGenerator<?> generator, @Nonnull TemplateManager templateManagerIn, int chunkX, int chunkZ, @Nonnull Biome biomeIn)
        {
            NoFeatureConfig nofeatureconfig = generator.getStructureConfig(biomeIn, Feature.END_CITY);
            int x = chunkX * 16;
            int z = chunkZ * 16;
            BlockPos blockpos = new BlockPos(x, 90, z);
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            ErasedPieces.initTemplate(templateManagerIn, blockpos, rotation, this.components, this.rand, nofeatureconfig);
            this.recalculateStructureSize();
        }

        @Override
        public void generateStructure(IWorld world, ChunkGenerator<?> generator, Random rand, MutableBoundingBox box, ChunkPos pos)
        {
            synchronized(this.components)
            {
                this.components.removeIf(structurepiece -> structurepiece.getBoundingBox().intersectsWith(box) && !structurepiece.create(world, generator, rand, box, pos));
                System.out.println(components.size() + " МНОГО");
                this.recalculateStructureSize();
            }
        }
    }
}