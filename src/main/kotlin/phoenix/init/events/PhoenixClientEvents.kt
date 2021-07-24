package phoenix.init.events

import net.minecraft.block.Block
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
import phoenix.api.block.IColoredBlock
import phoenix.client.render.OvenRenderer
import phoenix.client.render.TankRenderer
import phoenix.client.render.entity.*
import phoenix.init.PhxBlocks
import phoenix.init.PhxBlocks.blocks
import phoenix.init.PhxContainers
import phoenix.init.PhxEntities.ancientGolemEntity
import phoenix.init.PhxEntities.cauda
import phoenix.init.PhxEntities.explosiveBall
import phoenix.init.PhxEntities.iceBall
import phoenix.init.PhxEntities.talpa
import phoenix.init.PhxEntities.zirconiumKnife
import phoenix.init.PhxRenderTypes
import phoenix.init.PhxTiles
import phoenix.items.FixedSpawnEggItem
import phoenix.network.initPacketSystem
import phoenix.other.mc
import phoenix.other.rainbowColor
import phoenix.utils.ClientStageUppedEvent
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate

@EventBusSubscriber(modid = Phoenix.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object PhoenixClientEvents
{
    @SubscribeEvent
    fun onBlockColor(event: ColorHandlerEvent.Block)
    {
        for (block in blocks.getEntries().map(ObjectHolderDelegate<out Block>::get))
            if (block is IColoredBlock && block.getBlockColor() != null)
                event.blockColors.register(block.getBlockColor()!!, block)
    }

    @SubscribeEvent
    fun onItemColor(event : ColorHandlerEvent.Item)
    {
        for (block in blocks.getEntries().map(ObjectHolderDelegate<out Block>::get))
            if (block is IColoredBlock && block.getItemColor() != null)
                event.itemColors.register(block.getItemColor()!!, block)

        for (i in FixedSpawnEggItem.eggs)
            event.itemColors.register(i::getColor, i)
    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent)
    {
        initPacketSystem()
        PhxRenderTypes.init()
        PhxContainers.registerScreens()
        RenderTypeLookup.setRenderLayer(PhxBlocks.oven, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.bambooPipe, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.tank, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.armoredGlass, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.wetLog, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.wetSlab, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.wetStairs, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.setaJuice, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.juicer, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhxBlocks.potteryBarrel, RenderType.getTranslucent())
        RenderingRegistry.registerEntityRenderingHandler(talpa, ::TalpaRenderer)
        RenderingRegistry.registerEntityRenderingHandler(ancientGolemEntity, ::AncientGolemRenderer)
        RenderingRegistry.registerEntityRenderingHandler(iceBall, ::IceBallRenderer)
        RenderingRegistry.registerEntityRenderingHandler(cauda, ::CaudaRenderer)
        RenderingRegistry.registerEntityRenderingHandler(zirconiumKnife, ::KnifeRenderer)
        RenderingRegistry.registerEntityRenderingHandler(explosiveBall, ::ExplosiveBallRenderer)
        ClientRegistry.bindTileEntityRenderer(PhxTiles.tank, ::TankRenderer)
        ClientRegistry.bindTileEntityRenderer(PhxTiles.juicer, ::TankRenderer)
        ClientRegistry.bindTileEntityRenderer(PhxTiles.oven, ::OvenRenderer)

        mc!!.splashes.possibleSplashes.add(rainbowColor("God is an artist, since there are so many \n colors in the world")) //Reference to: Beautiful mind
        mc!!.splashes.possibleSplashes.add("$RED The essence of life is that it changes itself") //Reference to: Evangelion-3.33 you can(not) redo
        mc!!.splashes.possibleSplashes.add("$BLUE Bridge station is absent") //Reference to: Dovecote in a yellow glade
        mc!!.splashes.possibleSplashes.add("$DARK_BLUE Third child is an angel!!") //Reference to: Rebuild of Neon Genesis Evangelion
        mc!!.splashes.possibleSplashes.add("$GOLD Project E.N.D.") // Reference to: Phoenix project's old name
        mc!!.splashes.possibleSplashes.add("$AQUA Still, the first enemy of human is itself.") // Reference to: Neon Genesis Evangelion
        mc!!.splashes.possibleSplashes.add("$WHITE The hands of the clock cannot be turned back.$WHITE But it is in our power to move them forward!") // Reference to: Neon Genesis Evangelion
        mc!!.splashes.possibleSplashes.add("$RED Where are the fixes, Lebowski?") // Reference to: The Big Lebowski
    }

    @SubscribeEvent
    fun display(event : ClientStageUppedEvent)
    {

    }
}