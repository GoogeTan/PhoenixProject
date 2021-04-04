package phoenix.world.structures

import com.google.common.collect.ImmutableMap
import com.mojang.datafixers.Dynamic
import com.mojang.datafixers.types.DynamicOps
import mcp.MethodsReturnNonnullByDefault
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.feature.IFeatureConfig
import java.util.stream.IntStream
import java.util.stream.Stream
import javax.annotation.ParametersAreNonnullByDefault


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
class CustomEndSpikeConfig(
    val isCrystalInvulnerable: Boolean,
    val spikes: List<CustomEndSpike.EndSpike>,
    val crystalBeamTarget: BlockPos?
) :
    IFeatureConfig
{

    override fun <T> serialize(ops: DynamicOps<T>): Dynamic<T>
    {
        return Dynamic(
            ops, ops.createMap(
                ImmutableMap.of(
                    ops.createString("crystalInvulnerable"),
                    ops.createBoolean(isCrystalInvulnerable),
                    ops.createString("spikes"),
                    ops.createList(spikes.stream().map { spike: CustomEndSpike.EndSpike ->
                        spike.serialise(
                            ops
                        ).value
                    }),
                    ops.createString("crystalBeamTarget"),
                    if (crystalBeamTarget == null) ops.createList(Stream.empty()) else ops.createList(
                        IntStream.of(
                            crystalBeamTarget.x,
                            crystalBeamTarget.y,
                            crystalBeamTarget.z
                        ).mapToObj { value: Int -> ops.createInt(value) })
                )
            )
        )
    }

    companion object
    {
        fun <T> deserialize(dynamic: Dynamic<T>): CustomEndSpikeConfig
        {
            val list = dynamic["spikes"].asList(CustomEndSpike.EndSpike.Companion::deserialize)

            val list1 = dynamic["crystalBeamTarget"].asList { d: Dynamic<T> -> d.asInt(0) }

            val blockpos: BlockPos? = if (list1.size == 3) BlockPos(list1[0], list1[1], list1[2]) else null

            return CustomEndSpikeConfig(dynamic["crystalInvulnerable"].asBoolean(false), list, blockpos)
        }
    }
}
