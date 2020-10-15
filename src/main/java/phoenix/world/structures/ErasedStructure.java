package phoenix.world.structures;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.function.Function;

public class ErasedStructure extends ScatteredStructure<NoFeatureConfig>
{
    public ErasedStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn)
    {
        super(configFactoryIn);
    }

      
    @Override
    public String getStructureName()
    {
        return "erasedcity";
    }

    @Override
    public int getSize()
    {
        return 3;
    }

      
    @Override
    public Structure.IStartFactory getStartFactory()
    {
        return Start::new;
    }

    @Override
    protected int getSeedModifier()
    {
        return 14357618;
    }

    public static class Start extends StructureStart
    {
        public Start(Structure<?> structure, int chunkPosX, int chunkPosZ, MutableBoundingBox mbb, int references, long seed)
        {
            super(structure, chunkPosX, chunkPosZ, mbb, references, seed);
        }

        public void init(   ChunkGenerator<?> generator,    TemplateManager templateManagerIn, int chunkX, int chunkZ,    Biome biomeIn)
        {
            int i = chunkX * 16;
            int j = chunkZ * 16;
            BlockPos blockpos = new BlockPos(i, 90, j);
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];
            ErasedPieces.init(templateManagerIn, blockpos, rotation, this.components, this.rand);
            this.recalculateStructureSize();
        }
    }
}