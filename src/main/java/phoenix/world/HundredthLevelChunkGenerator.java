package phoenix.world;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.EndChunkGenerator;
import net.minecraft.world.gen.EndGenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import phoenix.init.PhoenixFeatures;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

public class HundredthLevelChunkGenerator extends EndChunkGenerator
{
    public HundredthLevelChunkGenerator(IWorld worldIn, BiomeProvider biomeProviderIn, EndGenerationSettings settingsIn)
    {
        super(worldIn, biomeProviderIn, settingsIn);
    }

    /*
    @Override
    public void generateStructures(@Nonnull BiomeManager biomeManager, @Nonnull IChunk chunk, @Nonnull ChunkGenerator<?> chunkGenerator, @Nonnull TemplateManager templateManager)
    {
        super.generateStructures(biomeManager, chunk, chunkGenerator, templateManager);
        Structure structure = (Structure) PhoenixFeatures.ERASED.get();
        StructureStart structurestart = chunk.getStructureStart(structure.getStructureName());
        int i = structurestart != null ? structurestart.getRefCount() : 0;
        SharedSeedRandom sharedseedrandom = new SharedSeedRandom();
        ChunkPos pos = chunk.getPos();
        StructureStart tmp = StructureStart.DUMMY;
        Biome biome = biomeManager.getBiome(new BlockPos(pos.getXStart() + 9, 0, pos.getZStart() + 9));
        if (structure.canBeGenerated(biomeManager, chunkGenerator, sharedseedrandom, pos.x, pos.z, biome)) {
            StructureStart structurestart2 = structure.getStartFactory().create(structure, pos.x, pos.z, MutableBoundingBox.getNewBoundingBox(), i, chunkGenerator.getSeed());
            structurestart2.init(this, templateManager, pos.x, pos.z, biome);
            tmp = structurestart2.isValid() ? structurestart2 : StructureStart.DUMMY;
        }

        chunk.putStructureStart(structure.getStructureName(), tmp);
    }*/

    @Nullable
    @Override
    public BlockPos findNearestStructure(World worldIn, String name, BlockPos pos, int radius, boolean skipExistingChunks)
    {
        Structure<?> structure = Feature.STRUCTURES.get(name.toLowerCase(Locale.ROOT));
        return structure == null ? null : structure.findNearest(worldIn, this, pos, radius, skipExistingChunks);
    }
}
