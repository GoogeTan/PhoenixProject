package phoenix.world.builders

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Blocks
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import phoenix.init.PhxBlocks

object Builders
{
    val UNDER: SurfaceBuilder<AdvancedSurfaceBuilderConfig> = SurfaceBuilder.register("under", UnderSurfaceBuilder { dynamic: Dynamic<*> -> AdvancedSurfaceBuilderConfig.deserialize(dynamic) })
    val HEARTVOID: SurfaceBuilder<SurfaceBuilderConfig> = SurfaceBuilder.register("heart", HeartVoidSurfaceBuilder { dynamic: Dynamic<*> -> SurfaceBuilderConfig.deserialize(dynamic) })
    val WET_HEARTVOID: SurfaceBuilder<SurfaceBuilderConfig> = SurfaceBuilder.register("wet_heart", WetHeartVoidSurfaceBuilder { dynamic: Dynamic<*> -> SurfaceBuilderConfig.deserialize(dynamic) })

    val UNDER_CONFIG = AdvancedSurfaceBuilderConfig(Blocks.END_STONE, Blocks.END_STONE, Blocks.END_STONE, PhxBlocks.fertileEndStone)
    val HEARTVOID_CONFIG = SurfaceBuilderConfig(Blocks.END_STONE.defaultState, Blocks.END_STONE.defaultState, Blocks.END_STONE.defaultState)
}