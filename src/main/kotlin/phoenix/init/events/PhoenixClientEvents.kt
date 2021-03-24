package phoenix.init.events

import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.util.text.TextFormatting.*
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import phoenix.Phoenix
import phoenix.client.render.OvenRenderer
import phoenix.client.render.PipeRender
import phoenix.client.render.TankRenderer
import phoenix.client.render.TextRenderer
import phoenix.client.render.entity.CaudaRenderer
import phoenix.client.render.entity.KnifeRenderer
import phoenix.client.render.entity.TalpaRenderer
import phoenix.init.PhoenixBlocks
import phoenix.init.PhoenixBlocks.BLOCKS
import phoenix.init.PhoenixContainers
import phoenix.init.PhoenixEntities.CAUDA
import phoenix.init.PhoenixEntities.KNIFE
import phoenix.init.PhoenixEntities.TALPA
import phoenix.init.PhoenixRenderTypes
import phoenix.init.PhoenixTiles
import phoenix.network.NetworkHandler
import phoenix.utils.StringUtils
import phoenix.utils.block.IColoredBlock
import phoenix.utils.mc

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Phoenix.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object PhoenixClientEvents
{
    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent)
    {
        NetworkHandler.init()
        PhoenixRenderTypes.init()
        PhoenixContainers.registerScreens()
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.OVEN, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.PIPE, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.TANK, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.ARMORED_GLASS, RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.TEXT_BLOCK,    RenderType.getCutoutMipped())
        RenderingRegistry.registerEntityRenderingHandler(TALPA, ::TalpaRenderer)
        RenderingRegistry.registerEntityRenderingHandler(CAUDA, ::CaudaRenderer)
        RenderingRegistry.registerEntityRenderingHandler(KNIFE, ::KnifeRenderer)
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.PIPE, ::PipeRender)
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.TANK, ::TankRenderer)
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.OVEN, ::OvenRenderer)
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.TEXT, ::TextRenderer)

        // регистрация цветных блоков
        for (block in BLOCKS.getEntries())
        {
            if (block is IColoredBlock)
            {
                if (block.getBlockColor() != null) mc.blockColors.register(block.getBlockColor()!!, block.get())
                if (block.getItemColor() != null) mc.itemColors.register(block.getItemColor()!!, block.get())
            }
        }

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