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

        return new EndChunkGenerator(this.world, new NewEndBiomeProvider(new EndBiomeProviderSettings(world.getWorldInfo()), (ServerWorld) world), settings);
    }
}