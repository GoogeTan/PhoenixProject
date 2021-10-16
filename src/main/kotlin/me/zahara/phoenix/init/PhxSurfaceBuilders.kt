package me.zahara.phoenix.init

import me.zahara.phoenix.biome.DefaultDecaySurfaceBuilder
import me.zahara.phoenix.init.registery.AutoRegisterer
import me.zahara.phoenix.modId
import net.minecraft.data.BuiltinRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderConfiguration
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber
object PhxSurfaceBuilders : AutoRegisterer<SurfaceBuilder<*>>(SurfaceBuilder::class.java)
{
    val default by register("default") { DefaultDecaySurfaceBuilder(SurfaceBuilderBaseConfiguration.CODEC) }
    val defaultConfig     = SurfaceBuilderBaseConfiguration(PhxBlocks.stone.defaultBlockState(), PhxBlocks.deepStone.defaultBlockState(), PhxBlocks.deepStone.defaultBlockState())
    val defaultConfigured = register("default_configured", default, defaultConfig)

    private val BUILDERS = mutableListOf<SurfaceBuilder<*>>()

    private fun<T : SurfaceBuilderConfiguration?> register(name: String, bilder:  SurfaceBuilder<T>, config : T): ConfiguredSurfaceBuilder<T>
    {
        bilder.registryName = ResourceLocation(modId, name)
        BUILDERS.add(bilder)
        return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER, name, bilder.configured(config))
    }

    @JvmStatic
    @SubscribeEvent
    fun subscribeBuilders(event : RegistryEvent.Register<SurfaceBuilder<*>>)
    {
        BUILDERS.forEach(event.registry::register)
    }
}