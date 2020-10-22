package phoenix.world;

import net.minecraft.block.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.EndBiomeProviderSettings;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.EndChunkGenerator;
import net.minecraft.world.gen.EndGenerationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.SaveHandler;

import java.io.File;

public class EndBiomedDimension extends EndDimension
{

    public EndBiomedDimension(World world, DimensionType type)
    {
        super(world, type);
    }

      
    @Override
    public ChunkGenerator<?> createChunkGenerator()
    {
        EndGenerationSettings settings = new EndGenerationSettings();
        settings.setDefaultBlock(Blocks.END_STONE.getDefaultState());
        settings.setDefaultFluid(Blocks.AIR.getDefaultState());
        settings.setSpawnPos(this.getSpawnCoordinate());
        initWorldSavedData((ServerWorld) world);
        return new EndChunkGenerator(this.world, new NewEndBiomeProvider(new EndBiomeProviderSettings(world.getWorldInfo()), world), settings);
    }

    private void initWorldSavedData(ServerWorld world)
    {
        File file1 = world.getDimension().getType().getDirectory(world.getSaveHandler().getWorldDirectory());
        File file2 = new File(file1, "data");
        file2.mkdirs();

        world.getChunkProvider().savedData = new DimensionSavedDataManager(file2, null);
    }
}