package phoenix.world.builders

import com.mojang.datafixers.Dynamic
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import phoenix.init.PhxBlocks
import java.util.*
import java.util.function.Function

class WetHeartVoidSurfaceBuilder(function: Function<Dynamic<*>, out SurfaceBuilderConfig>) : SurfaceBuilder<SurfaceBuilderConfig>(function)
{
    override fun buildSurface(random: Random, chunkIn: IChunk, biomeIn: Biome, x: Int, z: Int, startHeight: Int, noise: Double,
                              defaultBlock: BlockState, defaultFluid: BlockState, seaLevel: Int, seed: Long, config: SurfaceBuilderConfig)
    {
        var topBlock = config.top
        var middleBlock = config.under
        val currentPos = BlockPos.Mutable()
        var i = -1
        val noiseHeight = (noise / 3.0 + 3.0 + random.nextDouble() * 0.25).toInt()
        val currectX = x and 15
        val currectZ = z and 15
        for (y in startHeight downTo 0)
        {
            currentPos.setPos(currectX, y, currectZ)
            val currectState = chunkIn.getBlockState(currentPos)
            if (currectState.isAir)
            {
                i = -1
            } else if (currectState.block === defaultBlock.block)
            {
                if (!(isAir(chunkIn, currentPos.up(9)) || isAir(chunkIn, currentPos.down(9)) ||
                            isAir(chunkIn, currentPos.south(9)) || isAir(chunkIn, currentPos.north(9)) ||
                            isAir(chunkIn, currentPos.west(9)) || isAir(chunkIn, currentPos.east(9))))
                {
                    if (currectState.block === Blocks.END_STONE)
                    {
                        chunkIn.setBlockState(currentPos, (if (currentPos.y < 30) Blocks.WATER else  PhxBlocks.antiAir).defaultState, false)
                    }
                } else if (i == -1)
                {
                    if (noiseHeight <= 0)
                    {
                        topBlock = Blocks.AIR.defaultState
                        middleBlock = defaultBlock
                    }
                    i = noiseHeight
                    chunkIn.setBlockState(currentPos, topBlock, false)
                } else if (i > 0)
                {
                    --i
                    chunkIn.setBlockState(currentPos, middleBlock, false)
                }
            }
        }
    }

    inline fun isAir(chunk: IChunk, pos: BlockPos) = isAir(chunk.getBlockState(pos))
    inline fun isAir(state: BlockState) = state.isAir && state.block !== PhxBlocks.antiAir && state.block !== Blocks.WATER
}