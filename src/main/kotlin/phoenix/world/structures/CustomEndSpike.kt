package phoenix.world.structures

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Lists
import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.EntityType
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldWriter
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.Feature
import phoenix.world.StageManager.stageEnum
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.Function
import java.util.stream.Collectors
import java.util.stream.IntStream
import javax.annotation.ParametersAreNonnullByDefault
import kotlin.math.cos
import kotlin.math.sin


@ParametersAreNonnullByDefault
class CustomEndSpike : Feature<CustomEndSpikeConfig>({dynamic : Dynamic<*> -> CustomEndSpikeConfig.deserialize(dynamic)})
{
    override fun place(
        worldIn: IWorld,
        generator: ChunkGenerator<out GenerationSettings?>,
        rand: Random,
        pos: BlockPos,
        config: CustomEndSpikeConfig
    ): Boolean
    {
        var list = config.spikes
        if (list.isEmpty()) list = generateSpikes(worldIn)
        for (spike in list) if (spike.doesStartInChunk(pos)) placeSpike(worldIn, rand, config, spike)
        return true
    }

    /**
     * Places the End Spike in the world. Also generates the obsidian tower.
     */
    private fun placeSpike(worldIn: IWorld, rand: Random, config: CustomEndSpikeConfig, spike: EndSpike)
    {
        val radius = spike.radius
        for (blockpos in BlockPos.getAllInBoxMutable(
            BlockPos(spike.centerX - radius, 0, spike.centerZ - radius),
            BlockPos(spike.centerX + radius, spike.height + 10, spike.centerZ + radius)
        ))
        {
            if (blockpos.distanceSq(
                    spike.centerX.toDouble(),
                    blockpos.y.toDouble(),
                    spike.centerZ.toDouble(),
                    false
                ) <= (radius * radius + 1).toDouble() && blockpos.y < spike.height
            )
            {
                setBlockState(worldIn, blockpos, Blocks.OBSIDIAN.defaultState)
            } else if (blockpos.y > 65)
            {
                setBlockState(worldIn, blockpos, Blocks.AIR.defaultState)
            }
        }
        stageEnum.createTower(this, worldIn, spike)
        val crystal = EntityType.END_CRYSTAL.create(worldIn.world)
        if (crystal != null)
        {
            crystal.beamTarget = config.crystalBeamTarget
            crystal.isInvulnerable = config.isCrystalInvulnerable
            crystal.setLocationAndAngles(
                (spike.centerX.toFloat() + 0.5f).toDouble(),
                (spike.height + 1).toDouble(),
                (spike.centerZ.toFloat() + 0.5f).toDouble(),
                rand.nextFloat() * 360.0f,
                0.0f
            )
            worldIn.addEntity(crystal)
            setBlockState(worldIn, BlockPos(spike.centerX, spike.height, spike.centerZ), Blocks.BEDROCK.defaultState)
        }
    }

    class EndSpike(val centerX: Int, val centerZ: Int, val radius: Int, val height: Int, val isGuarded: Boolean)
    {
        fun doesStartInChunk(pos: BlockPos): Boolean = pos.x shr 4 == centerX shr 4 && pos.z shr 4 == centerZ shr 4

        fun <T> serialise(ops: DynamicOps<T>): Dynamic<T>
        {
            val builder: ImmutableMap.Builder<T, T> = ImmutableMap.builder()
            builder.put(ops.createString("centerX"), ops.createInt(centerX))
            builder.put(ops.createString("centerZ"), ops.createInt(centerZ))
            builder.put(ops.createString("radius"), ops.createInt(radius))
            builder.put(ops.createString("height"), ops.createInt(height))
            builder.put(ops.createString("guarded"), ops.createBoolean(isGuarded))
            return Dynamic(ops, ops.createMap(builder.build()))
        }

        companion object
        {
            fun <T> deserialize(dynamic: Dynamic<T>): EndSpike
            {
                return EndSpike(
                    dynamic["centerX"].asInt(0),
                    dynamic["centerZ"].asInt(0),
                    dynamic["radius"].asInt(0),
                    dynamic["height"].asInt(0),
                    dynamic["guarded"].asBoolean(false)
                )
            }
        }

    }

    class EndSpikeCacheLoader : CacheLoader<Long?, List<EndSpike>>()
    {
        override fun load(seed: Long?): List<EndSpike>
        {
            val list = IntStream.range(0, 10).boxed().collect(Collectors.toList())
            list.shuffle(Random(seed!!))
            val res: MutableList<EndSpike> = Lists.newArrayList()
            for (i in list.indices)
            {
                val centerX = MathHelper.floor(42.0 * cos(2.0 * (-Math.PI + Math.PI / 10.0 * i.toDouble())))
                val centerZ = MathHelper.floor(42.0 * sin(2.0 * (-Math.PI + Math.PI / 10.0 * i.toDouble())))
                val current = list[i]
                val radius = 2 + current / 3
                val height = 76 + current * 3
                val ifGuarded = current == 1 || current == 2
                res.add(EndSpike(centerX, centerZ, radius, height, ifGuarded))
            }
            return res
        }
    }

    public override fun setBlockState(worldIn: IWorldWriter, pos: BlockPos, state: BlockState)
    {
        worldIn.setBlockState(pos, state, 3)
    }

    companion object
    {
        var LOADING_CACHE: LoadingCache<Long, List<EndSpike>> =
            CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).build(
                EndSpikeCacheLoader()
            )

        fun generateSpikes(worldIn: IWorld): List<EndSpike>
        {
            val random = Random(worldIn.seed)
            val i = random.nextLong() and 65535L
            return LOADING_CACHE.getUnchecked(i)
        }
    }
}