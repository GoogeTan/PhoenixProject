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
import phoenix.utils.get
import java.util.*
import java.util.function.Function

class HeartVoidSurfaceBuilder(function: Function<Dynamic<*>, out SurfaceBuilderConfig>) : SurfaceBuilder<SurfaceBuilderConfig>(function)
{
    override fun buildSurface(random: Random, chunkIn: IChunk, biomeIn: Biome, x: Int, z: Int, startHeight: Int, noise: Double,
                              defaultBlock: BlockState, defaultFluid: BlockState, seaLevel: Int, seed: Long, config: SurfaceBuilderConfig)
    {
        var top_block = config.top
        var middle_block = config.under
        val currect_pos = BlockPos.Mutable()
        var i = -1
        val noiseHeight = (noise / 3.0 + 3.0 + random.nextDouble() * 0.25).toInt()
        val currectX = x and 15
        val currectZ = z and 15
        for (y in startHeight downTo 0)
        {
            currect_pos.setPos(currectX, y, currectZ)
            val currectState = chunkIn[currect_pos]
            if (currectState.isAir)
            {
                i = -1
            } else if (currectState.block === defaultBlock.block)
            {
                if (!(isAir(chunkIn, currect_pos.up(9)) || isAir(chunkIn, currect_pos.down(9)) ||
                                isAir(chunkIn, currect_pos.south(9)) || isAir(chunkIn, currect_pos.north(9)) ||
                                isAir(chunkIn, currect_pos.west(9)) || isAir(chunkIn, currect_pos.east(9))))
                {
                    if (currectState.block === Blocks.END_STONE)
                    {
                        chunkIn.setBlockState(currect_pos, PhxBlocks.antiAir.defaultState, false)
                    }
                } else if (i == -1)
                {
                    if (noiseHeight <= 0)
                    {
                        top_block = Blocks.AIR.defaultState
                        middle_block = defaultBlock
                    }
                    i = noiseHeight
                    chunkIn.setBlockState(currect_pos, top_block, false)
                } else if (i > 0)
                {
                    --i
                    chunkIn.setBlockState(currect_pos, middle_block, false)
                }
            }
        }
    }

    companion object
    {
        fun isAir(chunk: IChunk, pos: BlockPos) = isAir(chunk[pos])
        fun isAir(state: BlockState) = state.isAir && state.block !== PhxBlocks.antiAir
    }
}