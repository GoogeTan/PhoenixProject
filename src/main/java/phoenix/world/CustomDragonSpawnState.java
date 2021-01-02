package phoenix.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.end.DragonSpawnState;
import net.minecraft.world.server.ServerWorld;
import phoenix.init.PhoenixFeatures;
import phoenix.world.structures.CustomEndSpike;
import phoenix.world.structures.CustomEndSpikeConfig;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public enum  CustomDragonSpawnState
{
    START
    {
        public void process(ServerWorld world, CustomDragonFightManager manager, List<EnderCrystalEntity> list, int ticks, BlockPos pos)
        {
            BlockPos center = new BlockPos(0, 128, 0);
            if(list != null)
                list.stream().filter(Objects::nonNull).forEach(enderCrystalEntity -> enderCrystalEntity.setBeamTarget(center));
            /*
            if(list != null)
                for (EnderCrystalEntity entity : list)
                    if(entity != null)
                        entity.setBeamTarget(center);
             */
            manager.setRespawnState(DragonSpawnState.PREPARING_TO_SUMMON_PILLARS);
        }
    },
    PREPARING_TO_SUMMON_PILLARS
    {
        public void process(ServerWorld world, CustomDragonFightManager manager, List<EnderCrystalEntity> list, int ticks, BlockPos pos)
        {
            if (ticks < 100)
            {
                if (ticks == 0 || ticks == 50 || ticks == 51 || ticks == 52 || ticks >= 95)
                {
                    world.playEvent(3001, new BlockPos(0, 128, 0), 0);
                }
            } else
                {
                    manager.setRespawnState(DragonSpawnState.SUMMONING_PILLARS);
                }
        }
    },
    SUMMONING_PILLARS
            {
                public void process(ServerWorld world, CustomDragonFightManager manager, List<EnderCrystalEntity> list, int ticks, BlockPos pos)
                {
                    boolean isEnd = ticks % 40 == 0;
                    boolean isPrevEnd = ticks % 40 == 39;
                    if (isEnd || isPrevEnd)
                    {
                        List<CustomEndSpike.EndSpike> spikes = CustomEndSpike.generateSpikes(world);
                        int currentSpike = ticks / 40;
                        if (currentSpike < spikes.size())
                        {
                            CustomEndSpike.EndSpike spike = spikes.get(currentSpike);
                            if (isEnd)
                            {
                                list.forEach(enderCrystalEntity -> enderCrystalEntity.setBeamTarget(new BlockPos(spike.getCenterX(), spike.getHeight() + 1, spike.getCenterZ())));
                            } else
                            {
                                BlockPos.getAllInBoxMutable(
                                        new BlockPos(spike.getCenterX() - 10, spike.getHeight() - 10, spike.getCenterZ() - 10),
                                        new BlockPos(spike.getCenterX() + 10, spike.getHeight() + 10, spike.getCenterZ() + 10))
                                                .forEach(blockPos -> world.removeBlock(blockPos, false));

                                world.createExplosion(null, (float) spike.getCenterX() + 0.5F, spike.getHeight(), (float) spike.getCenterZ() + 0.5F, 5.0F, Explosion.Mode.DESTROY);
                                CustomEndSpikeConfig config = new CustomEndSpikeConfig(true, ImmutableList.of(spike), new BlockPos(0, 128, 0));
                                PhoenixFeatures.END_SPIKE.get().withConfiguration(config).place(world, world.getChunkProvider().getChunkGenerator(), new Random(), new BlockPos(spike.getCenterX(), 45, spike.getCenterZ()));
                            }
                        } else if (isEnd)
                        {
                            manager.setRespawnState(DragonSpawnState.SUMMONING_DRAGON);
                        }
                    }
                }
            },
    SUMMONING_DRAGON
            {
                public void process(ServerWorld world, CustomDragonFightManager manager, List<EnderCrystalEntity> list, int ticks, BlockPos pos)
                {
                    if (ticks >= 100)
                    {
                        manager.setRespawnState(DragonSpawnState.END);
                        manager.resetSpikeCrystals();

                        for (EnderCrystalEntity enderCrystalEntity : list)
                        {
                            enderCrystalEntity.setBeamTarget(null);
                            world.createExplosion(enderCrystalEntity, enderCrystalEntity.getPosX(), enderCrystalEntity.getPosY(), enderCrystalEntity.getPosZ(), 6.0F, Explosion.Mode.NONE);
                            enderCrystalEntity.remove();
                        }
                    } else if (ticks >= 80)
                    {
                        world.playEvent(3001, new BlockPos(0, 128, 0), 0);
                    } else if (ticks == 0)
                    {

                        for (EnderCrystalEntity enderCrystalEntity : list)
                        {
                            enderCrystalEntity.setBeamTarget(new BlockPos(0, 128, 0));
                        }
                    } else if (ticks < 5)
                    {
                        world.playEvent(3001, new BlockPos(0, 128, 0), 0);
                    }

                }
            },
    END
            {
                public void process(ServerWorld world, CustomDragonFightManager manager, List<EnderCrystalEntity> list, int ticks, BlockPos pos)
                {
                }
            };

    public abstract void process(ServerWorld var1, CustomDragonFightManager var2, List<EnderCrystalEntity> var3, int var4, BlockPos var5);
}
