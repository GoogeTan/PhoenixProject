package ru.googletan.phoenix.init;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import ru.googletan.phoenix.Phoenix;

@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID)
public class BlocksRegister
{
    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
        // register a new block here
        Phoenix.LOGGER.info("HELLO from Register Block");
    }
}
