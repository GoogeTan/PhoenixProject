@file:JvmName("CommonForge")
@file:Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
package me.zahara.phoenix.init.event

import me.zahara.phoenix.init.PhxDimensions
import net.minecraft.core.Registry
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent

@SubscribeEvent
fun serverStart(event: FMLServerAboutToStartEvent)
{
    val server = event.server
    PhxDimensions.registerDimensions(
        server.worldData.worldGenSettings().dimensions(),
        server.registryAccess().registryOrThrow(Registry.BIOME_REGISTRY),
        server.worldData.worldGenSettings().seed()
    )
}