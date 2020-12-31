package phoenix.world.builders

import com.mojang.datafixers.Dynamic
import net.minecraft.block.Blocks
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig
import phoenix.init.PhoenixBlocks

object Builders
{
    //Билдеры
    val UNDER: SurfaceBuilder<AdvancedSurfaceBuilderConfig> = SurfaceBuilder.register("under", UnderSurfaceBuilder { p_215455_0_: Dynamic<*>? -> AdvancedSurfaceBuilderConfig.deserialize(p_215455_0_) })
    val HEARTVOID: SurfaceBuilder<SurfaceBuilderConfig> = SurfaceBuilder.register("heart", HeartVoidSurfaceBuilder { p_215455_0_: Dynamic<*>? -> SurfaceBuilderConfig.deserialize(p_215455_0_) })

    //Конфиги для билдеров
    val UNDER_CONFIG = AdvancedSurfaceBuilderConfig(
        Blocks.END_STONE,
        Blocks.END_STONE,
        Blocks.END_STONE,
        PhoenixBlocks.FERTILE_END_STONE.get()
    )
    val HEARTVOID_CONFIG = SurfaceBuilderConfig(Blocks.END_STONE.defaultState, Blocks.END_STONE.defaultState, Blocks.END_STONE.defaultState)
}