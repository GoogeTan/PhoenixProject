package phoenix.world.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Blocks
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.NoFeatureConfig
import phoenix.init.PhxBlocks
import phoenix.utils.nextInt
import java.util.*

object WaterPlacesFeature : Feature<NoFeatureConfig>({ dyn: Dynamic<*>? -> NoFeatureConfig.deserialize(dyn) })
{
    override fun place(
        worldIn: IWorld,
        generator: ChunkGenerator<out GenerationSettings>,
        rand: Random,
        pos: BlockPos,
        config: NoFeatureConfig
    ): Boolean
    {
        fun place(pos : BlockPos, depth : Int = 0)
        {
            if (depth < 8)
            {
                var flag = true
                for (i in Direction.values())
                {
                    if (worldIn.isAirBlock(pos.offset(i)))
                        flag = false
                }

                if (flag)
                {
                    worldIn.setBlockState(pos, Blocks.WATER.defaultState, 3)
                    for (i in Direction.values())
                        if (worldIn.getBlockState(pos.offset(i)).block == Blocks.END_STONE && rand.nextInt(0, 10) < 3)
                            place(pos.offset(i), depth + rand.nextInt(0, 2))
                }
            }
            else if (depth == 8)
            {
                for (i in Direction.values())
                {
                    val position = pos.offset(i)
                    if (worldIn.getBlockState(pos.offset(i)).block == Blocks.END_STONE)
                        worldIn.setBlockState(position, PhxBlocks.fertileEndStone.defaultState, 3)
                }
            }
        }
        place(BlockPos(pos.x, 35 + rand.nextInt(-15, 15), pos.z))
        return false
    }
}