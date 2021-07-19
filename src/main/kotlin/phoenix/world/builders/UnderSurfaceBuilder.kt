package phoenix.world.builders

import com.mojang.datafixers.Dynamic
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.biome.Biome
import net.minecraft.world.chunk.IChunk
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import phoenix.other.get
import java.util.*
import java.util.function.Function
import javax.annotation.ParametersAreNonnullByDefault


@ParametersAreNonnullByDefault
class UnderSurfaceBuilder(function: Function<Dynamic<*>, out AdvancedSurfaceBuilderConfig>) : SurfaceBuilder<AdvancedSurfaceBuilderConfig>(function)
{
    override fun buildSurface(
        random: Random,
        chunkIn: IChunk,
        biomeIn: Biome,
        x: Int,
        z: Int,
        startHeight: Int,
        noise: Double,
        defaultBlock: BlockState,
        defaultFluid: BlockState,
        seaLevel: Int,
        seed: Long,
        config: AdvancedSurfaceBuilderConfig
    )
    {
        this.buildSurface(
            random,
            chunkIn,
            x,
            z,
            startHeight,
            noise,
            defaultBlock,
            config.top,
            config.under,
            config.advanced
        )
    }

    private fun buildSurface(random: Random, chunkIn: IChunk, x: Int, z: Int, startHeight: Int, noise: Double, defaultBlock: BlockState, top: BlockState, middle: BlockState, under: BlockState)
    {
        var topBlock = top
        var middleBlock = middle
        val currentPos = BlockPos.Mutable()
        var i = -1
        val noiseHeight = (noise / 3.0 + 3.0 + random.nextDouble() * 0.25).toInt()
        val currentX = x and 15
        val currentZ = z and 15
        for (y in startHeight downTo 0)
        {
            currentPos.setPos(currentX, y, currentZ)
            val currentState = chunkIn[currentPos]
            if (currentState.isAir)
            {
                i = -1
            } else if (currentState.block === defaultBlock.block)
            {
                if (y < 30 + random.nextInt(5))
                {
                    if (currentState.block === Blocks.END_STONE)
                    {
                        chunkIn.setBlockState(currentPos, under, false)
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
}
