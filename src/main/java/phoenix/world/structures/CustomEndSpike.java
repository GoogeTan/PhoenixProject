package phoenix.world.structures;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import phoenix.world.StageManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ParametersAreNonnullByDefault
public class CustomEndSpike extends Feature<CustomEndSpikeConfig>
{
    public static LoadingCache<Long, List<EndSpike>> LOADING_CACHE = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).build(new EndSpikeCacheLoader());

    public CustomEndSpike()
    {
        super(CustomEndSpikeConfig::deserialize);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, CustomEndSpikeConfig config)
    {
        List<EndSpike> list = config.getSpikes();
        if (list.isEmpty())
            list = generateSpikes(worldIn);


        for (EndSpike spike : list)
            if (spike.doesStartInChunk(pos))
                this.placeSpike(worldIn, rand, config, spike);
        return true;
    }

    public static List<EndSpike> generateSpikes(IWorld worldIn)
    {
        Random random = new Random(worldIn.getSeed());
        long i = random.nextLong() & 65535L;
        return LOADING_CACHE.getUnchecked(i);
    }

    /**
     * Places the End Spike in the world. Also generates the obsidian tower.
     */
    private void placeSpike(IWorld worldIn, Random rand, CustomEndSpikeConfig config, EndSpike spike)
    {
        int radius = spike.getRadius();

        for (BlockPos blockpos : BlockPos.getAllInBoxMutable(new BlockPos(spike.getCenterX() - radius, 0, spike.getCenterZ() - radius), new BlockPos(spike.getCenterX() + radius, spike.getHeight() + 10, spike.getCenterZ() + radius)))
        {
            if (blockpos.distanceSq(spike.getCenterX(), blockpos.getY(), spike.getCenterZ(), false) <= (double) (radius * radius + 1) && blockpos.getY() < spike.getHeight())
            {
                this.setBlockState(worldIn, blockpos, Blocks.OBSIDIAN.getDefaultState());
            }
            else if (blockpos.getY() > 65)
            {
                this.setBlockState(worldIn, blockpos, Blocks.AIR.getDefaultState());
            }
        }
        StageManager.getStageEnum().createTower(this, worldIn, spike);

        EnderCrystalEntity crystal = EntityType.END_CRYSTAL.create(worldIn.getWorld());
        if(crystal != null)
        {
            crystal.setBeamTarget(config.getCrystalBeamTarget());
            crystal.setInvulnerable(config.isCrystalInvulnerable());
            crystal.setLocationAndAngles((float) spike.getCenterX() + 0.5F, spike.getHeight() + 1, (float) spike.getCenterZ() + 0.5F, rand.nextFloat() * 360.0F, 0.0F);
            worldIn.addEntity(crystal);

            this.setBlockState(worldIn, new BlockPos(spike.getCenterX(), spike.getHeight(), spike.getCenterZ()), Blocks.BEDROCK.getDefaultState());
        }
    }

    public static class EndSpike
    {
        private final int centerX;
        private final int centerZ;
        private final int radius;
        private final int height;
        private final boolean guarded;
        private final AxisAlignedBB topBoundingBox;

        public EndSpike(int centerXIn, int centerZIn, int radiusIn, int heightIn, boolean guardedIn) {
            this.centerX = centerXIn;
            this.centerZ = centerZIn;
            this.radius = radiusIn;
            this.height = heightIn;
            this.guarded = guardedIn;
            this.topBoundingBox = new AxisAlignedBB((double)(centerXIn - radiusIn), 0.0D, (double)(centerZIn - radiusIn), (double)(centerXIn + radiusIn), 256.0D, (double)(centerZIn + radiusIn));
        }

        public boolean doesStartInChunk(BlockPos pos) {
            return pos.getX() >> 4 == this.centerX >> 4 && pos.getZ() >> 4 == this.centerZ >> 4;
        }

        public int getCenterX() {
            return this.centerX;
        }

        public int getCenterZ() {
            return this.centerZ;
        }

        public int getRadius() {
            return this.radius;
        }

        public int getHeight() {
            return this.height;
        }

        public boolean isGuarded() {
            return this.guarded;
        }

        public AxisAlignedBB getTopBoundingBox() {
            return this.topBoundingBox;
        }

        public <T> Dynamic<T> serialise(DynamicOps<T> ops) {
            ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
            builder.put(ops.createString("centerX"), ops.createInt(this.centerX));
            builder.put(ops.createString("centerZ"), ops.createInt(this.centerZ));
            builder.put(ops.createString("radius"), ops.createInt(this.radius));
            builder.put(ops.createString("height"), ops.createInt(this.height));
            builder.put(ops.createString("guarded"), ops.createBoolean(this.guarded));
            return new Dynamic<>(ops, ops.createMap(builder.build()));
        }

        public static <T> EndSpike deserialize(Dynamic<T> dynamic)
        {
            return new EndSpike(dynamic
                    .get("centerX").asInt(0),
                    dynamic.get("centerZ").asInt(0),
                    dynamic.get("radius").asInt(0),
                    dynamic.get("height").asInt(0),
                    dynamic.get("guarded").asBoolean(false));
        }
    }

    public static class EndSpikeCacheLoader extends CacheLoader<Long, List<EndSpike>>
    {
        public EndSpikeCacheLoader()
        {
        }

        public List<EndSpike> load(Long seed)
        {
            List<Integer> list = IntStream.range(0, 10).boxed().collect(Collectors.toList());
            Collections.shuffle(list, new Random(seed));
            List<EndSpike> res = Lists.newArrayList();

            for (int i = 0; i < list.size(); ++i)
            {
                int centerX = MathHelper.floor(42.0D * Math.cos(2.0D * (-Math.PI + (Math.PI / 10D) * (double) i)));
                int centerZ = MathHelper.floor(42.0D * Math.sin(2.0D * (-Math.PI + (Math.PI / 10D) * (double) i)));
                int current = list.get(i);
                int radius = 2 + current / 3;
                int height = 76 + current * 3;
                boolean ifGuarded = current == 1 || current == 2;
                res.add(new EndSpike(centerX, centerZ, radius, height, ifGuarded));
            }

            return res;
        }
    }

    public void setBlockState(IWorldWriter worldIn, BlockPos pos, BlockState state) {
        worldIn.setBlockState(pos, state, 3);
    }
}