@file:Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
package me.zahara.phoenix.init.event

import me.zahara.phoenix.Phoenix
import me.zahara.phoenix.init.PhxBlocks
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@SubscribeEvent
fun commonSetup(event: RegistryEvent.Register<Item>)
{
    PhxBlocks.forEach { block : Block ->
        val item = BlockItem(block, Item.Properties().tab(Phoenix.creativeModeTab))
        item.registryName = block.registryName
        event.registry.register(item)
    }
}
