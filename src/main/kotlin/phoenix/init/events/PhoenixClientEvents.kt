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
import phoenix.client.render.entity.KnifeRenderer
import phoenix.client.render.entity.TalpaRenderer
import phoenix.init.PhoenixBlocks
import phoenix.init.PhoenixBlocks.BLOCKS
import phoenix.init.PhoenixContainers
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
    @JvmStatic
    fun onClientSetup(event: FMLClientSetupEvent)
    {
        NetworkHandler.init()
        PhoenixRenderTypes.init()
        PhoenixContainers.registerScreens()
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.OVEN.get(), RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.PIPE.get(), RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.TANK.get(), RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.ARMORED_GLASS.get(), RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.TEXT_BLOCK.get(),    RenderType.getCutoutMipped())
        RenderingRegistry.registerEntityRenderingHandler(TALPA.get(), ::TalpaRenderer)
        //RenderingRegistry.registerEntityRenderingHandler(CAUDA.get(), ::CaudaRenderer)
        RenderingRegistry.registerEntityRenderingHandler(KNIFE.get(), ::KnifeRenderer)
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.PIPE.get(), ::PipeRender)
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.TANK.get(), ::TankRenderer)
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.OVEN.get(), ::OvenRenderer)
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.TEXT.get(), ::TextRenderer)

        // регистрация цветных блоков
        for (block in BLOCKS.entries)
        {
            val colorBlock = block.get()
            if (colorBlock is IColoredBlock)
            {
                if (colorBlock.getBlockColor() != null) mc.blockColors.register(colorBlock.getBlockColor()!!, block.get())
                if (colorBlock.getItemColor()  != null) mc.itemColors.register(colorBlock.getItemColor()!!, block.get())
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