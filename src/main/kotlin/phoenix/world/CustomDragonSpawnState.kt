package phoenix.world

import com.google.common.collect.ImmutableList
import net.minecraft.entity.item.EnderCrystalEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Explosion
import net.minecraft.world.end.DragonSpawnState
import net.minecraft.world.server.ServerWorld
import phoenix.init.PhoenixFeatures.END_SPIKE
import phoenix.world.structures.CustomEndSpike
import phoenix.world.structures.CustomEndSpikeConfig
import java.util.*
import java.util.function.Consumer


enum class CustomDragonSpawnState
{
    START
    {
        override fun process(world: ServerWorld, manager: CustomDragonFightManager, list: List<EnderCrystalEntity?>, ticks: Int, pos: BlockPos)
        {
            val center = BlockPos(0, 128, 0)
            list.stream().filter { obj: EnderCrystalEntity? -> Objects.nonNull(obj) }?.forEach { enderCrystalEntity: EnderCrystalEntity? -> enderCrystalEntity?.beamTarget = center }
            /*
if(list != null)
    for (EnderCrystalEntity entity : list)
        if(entity != null)
            entity.setBeamTarget(center);
*/manager.setRespawnState(DragonSpawnState.PREPARING_TO_SUMMON_PILLARS)
        }
    },
    PREPARING_TO_SUMMON_PILLARS
    {
        override fun process(world: ServerWorld, manager: CustomDragonFightManager, list: List<EnderCrystalEntity?>, ticks: Int, pos: BlockPos)
        {
            if (ticks < 100)
            {
                if (ticks == 0 || ticks == 50 || ticks == 51 || ticks == 52 || ticks >= 95)
                {
                    world.playEvent(3001, BlockPos(0, 128, 0), 0)
                }
            }
            else
            {
                manager.setRespawnState(DragonSpawnState.SUMMONING_PILLARS)
            }
        }
    },
    SUMMONING_PILLARS
    {
        override fun process(world: ServerWorld, manager: CustomDragonFightManager, list: List<EnderCrystalEntity?>, ticks: Int, pos: BlockPos)
        {
            val isEnd = ticks % 40 == 0
            val isPrevEnd = ticks % 40 == 39
            if (isEnd || isPrevEnd)
            {
                val spikes = CustomEndSpike.generateSpikes(world)
                val currentSpike = ticks / 40
                if (currentSpike < spikes.size)
                {
                    val spike = spikes[currentSpike]
                    if (isEnd)
                    {
                        list.forEach(Consumer { enderCrystalEntity: EnderCrystalEntity? -> enderCrystalEntity?.beamTarget = BlockPos(spike.centerX, spike.height + 1, spike.centerZ) })
                    }
                    else
                    {
                        BlockPos.getAllInBoxMutable(
                                BlockPos(spike.centerX - 10, spike.height - 10, spike.centerZ - 10),
                                BlockPos(spike.centerX + 10, spike.height + 10, spike.centerZ + 10))
                                .forEach(Consumer { blockPos: BlockPos -> world.removeBlock(blockPos, false) })
                        world.createExplosion(null, (spike.centerX.toFloat() + 0.5f).toDouble(), spike.height.toDouble(), (spike.centerZ.toFloat() + 0.5f).toDouble(), 5.0f, Explosion.Mode.DESTROY)
                        val config = CustomEndSpikeConfig(true, ImmutableList.of(spike), BlockPos(0, 128, 0))
                        END_SPIKE.withConfiguration(config).place(world, world.chunkProvider.chunkGenerator, Random(), BlockPos(spike.centerX, 45, spike.centerZ))
                    }
                }
                else if (isEnd)
                {
                    manager.setRespawnState(DragonSpawnState.SUMMONING_DRAGON)
                }
            }
        }
    },
    SUMMONING_DRAGON
    {
        override fun process(world: ServerWorld, manager: CustomDragonFightManager, list: List<EnderCrystalEntity?>, ticks: Int, pos: BlockPos)
        {
            when
            {
                ticks >= 100 ->
                {
                    manager.setRespawnState(DragonSpawnState.END)
                    manager.resetSpikeCrystals()
                    for (enderCrystalEntity in list)
                    {
                        enderCrystalEntity!!.beamTarget = null
                        world.createExplosion(enderCrystalEntity, enderCrystalEntity.posX, enderCrystalEntity.posY, enderCrystalEntity.posZ, 6.0f, Explosion.Mode.NONE)
                        enderCrystalEntity.remove()
                    }
                }
                ticks >= 80 ->
                {
                    world.playEvent(3001, BlockPos(0, 128, 0), 0)
                }
                ticks == 0 ->
                {
                    for (enderCrystalEntity in list)
                    {
                        enderCrystalEntity?.beamTarget = BlockPos(0, 128, 0)
                    }
                }
                ticks < 5 ->
                {
                    world.playEvent(3001, BlockPos(0, 128, 0), 0)
                }
            }
        }
    },
    END
    {
        override fun process(world: ServerWorld, manager: CustomDragonFightManager, list: List<EnderCrystalEntity?>, ticks: Int, pos: BlockPos)
        {
        }
    };

    abstract fun process(world: ServerWorld, manager: CustomDragonFightManager, list: List<EnderCrystalEntity?>, ticks: Int, pos: BlockPos)
}
