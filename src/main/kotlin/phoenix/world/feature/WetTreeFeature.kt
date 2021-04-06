package phoenix.world.feature

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.gen.ChunkGenerator
import net.minecraft.world.gen.GenerationSettings
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.NoFeatureConfig
import phoenix.init.PhoenixBlocks
import java.lang.Math.abs
import java.util.*
import java.util.function.Function

class WetTreeFeature : Feature<NoFeatureConfig>(Function { dyn: Dynamic<*>? -> NoFeatureConfig.deserialize(dyn) })
{
    override fun place(
        worldIn: IWorld,
        generator: ChunkGenerator<out GenerationSettings>,
        rand: Random,
        pos: BlockPos,
        config: NoFeatureConfig
    ): Boolean
    {
        var startPos = BlockPos(pos.x, 40, pos.z)

        if(!worldIn.isAirBlock(startPos)) return false

        while(worldIn.isAirBlock(startPos))
            if(startPos.y < 14) return false
            else
                startPos = startPos.down()
        startPos = startPos.up()
        var prevPos = startPos
        while (worldIn.isAirBlock(prevPos))
            if(prevPos.y > 58) return false
            else
                prevPos = prevPos.up()
        startPos = startPos.down()
        do
        {
            worldIn.setBlockState(startPos, PhoenixBlocks.WET_LOG.defaultState, 2)
            worldIn.setBlockState(startPos.east(), PhoenixBlocks.WET_LOG.defaultState, 2)
            worldIn.setBlockState(startPos.north(), PhoenixBlocks.WET_LOG.defaultState, 2)
            worldIn.setBlockState(startPos.east().north(), PhoenixBlocks.WET_LOG.defaultState, 2)

            startPos = startPos.up()
        }
        while (worldIn.isAirBlock(startPos) && startPos.y <= prevPos.y)

        return true
    }
}