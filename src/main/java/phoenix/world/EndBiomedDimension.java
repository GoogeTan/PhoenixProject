package phoenix.world;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.biome.provider.EndBiomeProviderSettings;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.EndDimension;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.EndChunkGenerator;
import net.minecraft.world.gen.EndGenerationSettings;

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
        return new HundredthLevelChunkGenerator(this.world, new NewEndBiomeProvider(new EndBiomeProviderSettings(world.getWorldInfo()), world), settings);
    }
}