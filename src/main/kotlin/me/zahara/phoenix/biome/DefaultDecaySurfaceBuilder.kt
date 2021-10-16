package me.zahara.phoenix.biome

import com.mojang.serialization.Codec
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration
import java.util.*

class DefaultDecaySurfaceBuilder(codec: Codec<SurfaceBuilderBaseConfiguration>) : SurfaceBuilder<SurfaceBuilderBaseConfiguration>(codec)
{
    override fun apply(
        rand: Random,
        chunk: ChunkAccess,
        biome: Biome,
        x: Int,
        z: Int,
        height: Int,
        noise: Double,
        defaultBlock: BlockState,
        defaultFluid: BlockState,
        seaLevel: Int,
        minSurfaceLevel: Int,
        seed: Long,
        config: SurfaceBuilderBaseConfiguration
    )
    {
        val pos = MutableBlockPos()

        var floorHeight = 10000
        for (y in height downTo minSurfaceLevel)
        {
            pos.set(x, y, z)
            val state: BlockState = chunk.getBlockState(pos)

            if (floorHeight == 10000 && !state.isAir)
                floorHeight = y
            val stateToPlace = when
            {
                y > (floorHeight - minSurfaceLevel) * 2.0 / 3.0 -> config.topMaterial
                y > (floorHeight - minSurfaceLevel) / 3.0       -> state
                else                                            -> config.underMaterial
            }
            chunk.setBlockState(pos, stateToPlace, false)
        }
    }
}