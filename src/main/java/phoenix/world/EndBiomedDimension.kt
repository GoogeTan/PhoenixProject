package phoenix.world

import net.minecraft.block.Blocks
import net.minecraft.world.World
import net.minecraft.world.biome.provider.EndBiomeProviderSettings
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.dimension.EndDimension
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.EndChunkGenerator
import net.minecraft.world.gen.EndGenerationSettings
import net.minecraft.world.server.ServerWorld
import javax.annotation.Nonnull

class EndBiomedDimension(world: World, type: DimensionType) : EndDimension(world, type)
{
    @Nonnull
    override fun createChunkGenerator(): ChunkGenerator<*>
    {
        val settings = EndGenerationSettings()
        settings.defaultBlock = Blocks.END_STONE.defaultState
        settings.defaultFluid = Blocks.AIR.defaultState
        if (spawnCoordinate != null)
            settings.spawnPos = this.spawnCoordinate!!
        return EndChunkGenerator(world, NewEndBiomeProvider(EndBiomeProviderSettings(world.worldInfo), (world as ServerWorld)), settings)
    }
}