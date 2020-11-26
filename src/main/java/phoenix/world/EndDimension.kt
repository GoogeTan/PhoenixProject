package phoenix.world

import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.ChunkPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.biome.provider.EndBiomeProviderSettings
import net.minecraft.world.dimension.Dimension
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.gen.EndChunkGenerator
import net.minecraft.world.gen.EndGenerationSettings
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import java.util.*


open class EndDimension(worldIn: World, typeIn: DimensionType) : Dimension(worldIn, typeIn, 0.0f)
{
    val SPAWN = BlockPos(100, 50, 0)
    val dragonFightManager: CustomDragonFightManager? = if (worldIn is ServerWorld) CustomDragonFightManager(worldIn, worldIn.worldInfo.getDimensionData(typeIn).getCompound("DragonFight"), this) else null

    override fun createChunkGenerator(): EndChunkGenerator
    {
        val settings = EndGenerationSettings()
        settings.defaultBlock = Blocks.END_STONE.defaultState
        settings.defaultFluid = Blocks.AIR.defaultState
        settings.spawnPos = this.spawnCoordinate
        return EndChunkGenerator(world, NewEndBiomeProvider(EndBiomeProviderSettings(world.worldInfo), (world as ServerWorld)), settings)
    }

    override fun calculateCelestialAngle(worldTime: Long, partialTicks: Float) = 0.0f
    @OnlyIn(Dist.CLIENT)
    override fun calcSunriseSunsetColors(celestialAngle: Float, partialTicks: Float) = null
    @OnlyIn(Dist.CLIENT)
    override fun isSkyColored() = false
    override fun canRespawnHere() = false
    override fun isSurfaceWorld() = false
    @OnlyIn(Dist.CLIENT)
    override fun getCloudHeight() = 8.0f
    override fun getSpawnCoordinate() = SPAWN
    override fun findSpawn(posX: Int, posZ: Int, checkValid: Boolean) = findSpawn(ChunkPos(posX shr 4, posZ shr 4), checkValid)
    @OnlyIn(Dist.CLIENT)
    override fun doesXZShowFog(x: Int, z: Int) = false

    @OnlyIn(Dist.CLIENT)
    override fun getFogColor(celestialAngle: Float, partialTicks: Float): Vec3d
    {
        val i = 10518688
        var f = MathHelper.cos(celestialAngle * (Math.PI.toFloat() * 2f)) * 2.0f + 0.5f
        f = MathHelper.clamp(f, 0.0f, 1.0f)
        var f1 = 0.627451f
        var f2 = 0.5019608f
        var f3 = 0.627451f
        f1 *= (f * 0.0f + 0.15f)
        f2 *= (f * 0.0f + 0.15f)
        f3 *= (f * 0.0f + 0.15f)
        return Vec3d(f1.toDouble(), f2.toDouble(), f3.toDouble())
    }

    override fun findSpawn(chunkPosIn: ChunkPos, checkValid: Boolean): BlockPos?
    {
        val random = Random(world.seed)
        val res = BlockPos(chunkPosIn.xStart + random.nextInt(15), 0, chunkPosIn.zEnd + random.nextInt(15))
        return if (world.getGroundAboveSeaLevel(res).material.blocksMovement()) res else null
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
}
