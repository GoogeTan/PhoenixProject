package phoenix.world

import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.biome.provider.EndBiomeProviderSettings
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.dimension.EndDimension
import net.minecraft.world.gen.EndChunkGenerator
import net.minecraft.world.gen.EndGenerationSettings
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import java.util.*

class EndDimension(worldIn: World, typeIn: DimensionType) : EndDimension(worldIn, typeIn)
{
    private val spawn = BlockPos(100, 50, 0)
    lateinit var biomeProvider : NewEndBiomeProvider
    override fun createChunkGenerator(): EndChunkGenerator
    {
        val settings = EndGenerationSettings()
        settings.defaultBlock = Blocks.END_STONE.defaultState
        settings.defaultFluid = Blocks.AIR.defaultState
        settings.spawnPos = this.spawnCoordinate
        biomeProvider = NewEndBiomeProvider(EndBiomeProviderSettings(world.worldInfo))
        return EndChunkGenerator(world, biomeProvider, settings)
    }

    init
    {
        this.dragonFightManager = if (worldIn is ServerWorld) CustomDragonFightManager(worldIn, worldIn.worldInfo.getDimensionData(typeIn).getCompound("DragonFight"), this) else null
    }

    override fun calculateCelestialAngle(worldTime: Long, partialTicks: Float) = 0.0f
    override fun canRespawnHere() = false
    override fun isSurfaceWorld() = false
    override fun getSpawnCoordinate() = spawn
    override fun findSpawn(posX: Int, posZ: Int, checkValid: Boolean) = findSpawn(ChunkPos(posX shr 4, posZ shr 4), checkValid)
    @OnlyIn(Dist.CLIENT)
    override fun calcSunriseSunsetColors(celestialAngle: Float, partialTicks: Float) = null
    @OnlyIn(Dist.CLIENT)
    override fun isSkyColored()   = false
    @OnlyIn(Dist.CLIENT)
    override fun getCloudHeight() = 8.0f
    @OnlyIn(Dist.CLIENT)
    override fun doesXZShowFog(x: Int, z: Int) = false
    @OnlyIn(Dist.CLIENT)
    override fun getFogColor(celestialAngle: Float, partialTicks: Float) = Vec3d(0.09411765, 0.07529412, 0.09411765)

    override fun findSpawn(chunkPosIn: ChunkPos, checkValid: Boolean): BlockPos?
    {
        val random = Random(world.seed)
        val pos = BlockPos(chunkPosIn.xStart + random.nextInt(15), 0, chunkPosIn.zEnd + random.nextInt(15))
        return if (world.getGroundAboveSeaLevel(pos).material.blocksMovement()) pos else null
    }

    override fun onWorldSave()
    {
        val nbt = CompoundNBT()
        if (dragonFightManager != null)
        {
            nbt.put("DragonFight", dragonFightManager.write())
        }
        world.worldInfo.setDimensionData(world.getDimension().type, nbt)
    }

    override fun tick()
    {
        dragonFightManager?.tick()
    }

    override fun getDragonFightManager(): CustomDragonFightManager
    {
        return dragonFightManager as CustomDragonFightManager
    }
}
