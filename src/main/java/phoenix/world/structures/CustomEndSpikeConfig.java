package phoenix.world.structures;


import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.IFeatureConfig;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CustomEndSpikeConfig implements IFeatureConfig
{
    private final boolean crystalInvulnerable;
    private final List<CustomEndSpike.EndSpike> spikes;
    @Nullable
    private final BlockPos crystalBeamTarget;

    public CustomEndSpikeConfig(boolean crystalInvulnerable, List<CustomEndSpike.EndSpike> spikes, @Nullable BlockPos crystalBeamTarget) {
        this.crystalInvulnerable = crystalInvulnerable;
        this.spikes = spikes;
        this.crystalBeamTarget = crystalBeamTarget;
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("crystalInvulnerable"),
                ops.createBoolean(this.crystalInvulnerable),
                ops.createString("spikes"),
                ops.createList(this.spikes.stream().map((spike) -> spike.serialise(ops).getValue())),
                ops.createString("crystalBeamTarget"),
                this.crystalBeamTarget == null ? ops.createList(Stream.empty()) : ops.createList(IntStream.of(this.crystalBeamTarget.getX(),
                this.crystalBeamTarget.getY(),
                this.crystalBeamTarget.getZ()).mapToObj(ops::createInt)))));
    }

    public static <T> CustomEndSpikeConfig deserialize(Dynamic<T> dynamic) {
        List<CustomEndSpike.EndSpike> list = dynamic.get("spikes").asList(CustomEndSpike.EndSpike::deserialize);
        List<Integer> list1 = dynamic.get("crystalBeamTarget").asList((d) -> d.asInt(0));
        BlockPos blockpos;
        if (list1.size() == 3) {
            blockpos = new BlockPos(list1.get(0), list1.get(1), list1.get(2));
        } else {
            blockpos = null;
        }

        return new CustomEndSpikeConfig(dynamic.get("crystalInvulnerable").asBoolean(false), list, blockpos);
    }

    public boolean isCrystalInvulnerable() {
        return this.crystalInvulnerable;
    }

    public List<CustomEndSpike.EndSpike> getSpikes() {
        return this.spikes;
    }

    @Nullable
    public BlockPos getCrystalBeamTarget() {
        return this.crystalBeamTarget;
    }
}
