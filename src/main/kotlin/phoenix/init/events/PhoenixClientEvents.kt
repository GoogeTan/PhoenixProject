package phoenix.init.events

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.util.text.TextFormatting.*
import net.minecraftforge.client.event.ColorHandlerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import phoenix.Phoenix
import phoenix.client.render.EnderCrystalRenderer
import phoenix.client.render.OvenRenderer
import phoenix.client.render.TankRenderer
import phoenix.client.render.dragon.AshDragonRenderer
import phoenix.client.render.dragon.RedoDragonRenderer
import phoenix.client.render.entity.*
import phoenix.init.*
import phoenix.init.PhxBlocks.blocks
import phoenix.init.PhxEntities.ancientGolemEntity
import phoenix.init.PhxEntities.cauda
import phoenix.init.PhxEntities.dragonAshStage
import phoenix.init.PhxEntities.dragonRedoStage
import phoenix.init.PhxEntities.enderCrystal
import phoenix.init.PhxEntities.explosiveBall
import phoenix.init.PhxEntities.talpa
import phoenix.init.PhxEntities.zirconiumKnife
import phoenix.items.FixedSpawnEggItem
import phoenix.network.NetworkHandler
import phoenix.utils.StringUtils
import phoenix.utils.block.IColoredBlock
import phoenix.utils.mc

@EventBusSubscriber(modid = Phoenix.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object PhoenixClientEvents
{
    @SubscribeEvent
    fun onBlockColor(event: ColorHandlerEvent.Block)
    {
        for (block in blocks.getEntries().map { b -> b.get() })
            if (block is IColoredBlock && block.getBlockColor() != null)
                event.blockColors.register(block.getBlockColor()!!, block)
    }

    @SubscribeEvent
    fun onItemColor(event : ColorHandlerEvent.Item)
    {
        for (block in blocks.getEntries().map { b -> b.get() })
            if (block is IColoredBlock && block.getItemColor() != null)
                event.itemColors.register(block.getItemColor()!!, block)

        for (i in FixedSpawnEggItem.eggs)
            event.itemColors.register({ _, index -> i.getColor(index) }, i)
    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent)
    {
        NetworkHandler.init()
        PhxRenderTypes.init()
        PhxContainers.registerScreens()
        RenderTypeLookup.setRenderLayer(PhxBlocks.oven, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.bambooPipe, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.tank, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.armoredGlass, RenderType.getCutoutMipped())
        //RenderTypeLookup.setRenderLayer(PhxBlocks.textBlock,    RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.wetLog, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.wetSlab, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.wetStairs, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.setaJuice, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.juicer, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.potteryBarrel, RenderType.getTranslucent())
        RenderingRegistry.registerEntityRenderingHandler(talpa, ::TalpaRenderer)
        RenderingRegistry.registerEntityRenderingHandler(ancientGolemEntity, ::AncientGolemRenderer)
        RenderingRegistry.registerEntityRenderingHandler(cauda, ::CaudaRenderer)
        RenderingRegistry.registerEntityRenderingHandler(zirconiumKnife, ::KnifeRenderer)
        RenderingRegistry.registerEntityRenderingHandler(dragonAshStage, ::AshDragonRenderer)
        RenderingRegistry.registerEntityRenderingHandler(dragonRedoStage, ::RedoDragonRenderer)
        RenderingRegistry.registerEntityRenderingHandler(dragonAshStage, ::AshDragonRenderer)
        RenderingRegistry.registerEntityRenderingHandler(explosiveBall, ::ExplosiveBallRenderer)
        RenderingRegistry.registerEntityRenderingHandler(enderCrystal, ::EnderCrystalRenderer)
        ClientRegistry.bindTileEntityRenderer(PhxTiles.tank, ::TankRenderer)
        ClientRegistry.bindTileEntityRenderer(PhxTiles.juicer, ::TankRenderer)
        ClientRegistry.bindTileEntityRenderer(PhxTiles.oven, ::OvenRenderer)
        //ClientRegistry.bindTileEntityRenderer(PhxTiles.TEXT, ::TextRenderer)



        val splashes = mc.splashes
        splashes.possibleSplashes.add(StringUtils.rainbowColor("God is an artist, since there are so many \n colors in the world")) //Reference to: Beautiful mind
        splashes.possibleSplashes.add("$RED The essence of life is that it changes itself") //Reference to: Evangelion-3.33 you can(not) redo
        splashes.possibleSplashes.add("$BLUE Bridge station is absent") //Reference to: Dovecote in a yellow glade
        splashes.possibleSplashes.add("$GRAY You can be wind... be forever.") //Reference to: Dovecote in a yellow glade
        splashes.possibleSplashes.add("$DARK_BLUE Third child is an angel!!") //Reference to: Neon Genesis Evangelion
        splashes.possibleSplashes.add("$GOLD Project E.N.D.") // Reference to: Phoenix project's old name
        splashes.possibleSplashes.add("$AQUA Still, the first enemy of human is itself.") // Reference to: Neon Genesis Evangelion
        splashes.possibleSplashes.add("$WHITE The hands of the clock cannot be turned back.$WHITE But it is in our power to move them forward!") // Reference to: Neon Genesis Evangelion
        splashes.possibleSplashes.add("$RED Where are the fixes, Lebowski?") // Reference to: The Big Lebowski
    }
}