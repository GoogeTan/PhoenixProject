package phoenix.init.events

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import phoenix.Phoenix
import phoenix.client.render.OvenRenderer
import phoenix.client.render.entity.KnifeRenderer
import phoenix.client.render.entity.TalpaRenderer
import phoenix.init.PhoenixBlocks
import phoenix.init.PhoenixBlocks.BLOCKS
import phoenix.init.PhoenixContainers
import phoenix.init.PhoenixEntities.KNIFE
import phoenix.init.PhoenixEntities.TALPA
import phoenix.init.PhoenixRenderTypes
import phoenix.init.PhoenixTiles
import phoenix.integration.minecraft.PhoenixRecipeBookCategories
import phoenix.network.NetworkHandler
import phoenix.utils.LogManager
import phoenix.utils.block.IColoredBlock

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
        //RenderTypeLookup.setRenderLayer(PhoenixBlocks.PIPE.get(), RenderType.getCutoutMipped())
        //RenderTypeLookup.setRenderLayer(PhoenixBlocks.TANK.get(), RenderType.getCutoutMipped())
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.ARMORED_GLASS.get(), RenderType.getCutoutMipped())
        //RenderTypeLookup.setRenderLayer(PhoenixBlocks.TEXT_BLOCK.get(),    RenderType.getCutoutMipped())
        RenderingRegistry.registerEntityRenderingHandler(TALPA.get(), ::TalpaRenderer)
        //RenderingRegistry.registerEntityRenderingHandler(CAUDA.get(), ::CaudaRenderer)
        RenderingRegistry.registerEntityRenderingHandler(KNIFE.get(), ::KnifeRenderer)
        //ClientRegistry.bindTileEntityRenderer(PhoenixTiles.PIPE.get(), ::PipeRender)
        //ClientRegistry.bindTileEntityRenderer(PhoenixTiles.TANK.get(), ::TankRenderer)
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.OVEN.get(), ::OvenRenderer)
        //ClientRegistry.bindTileEntityRenderer(PhoenixTiles.TEXT.get(), ::TextRenderer)

        // регистрация цветных блоков
        for (block in BLOCKS.entries)
        {
            val colorBlock = block.get()
            if (colorBlock is IColoredBlock)
            {
                if (colorBlock.blockColor != null) Minecraft.getInstance().blockColors.register(colorBlock.blockColor, block.get())
                if (colorBlock.itemColor != null) Minecraft.getInstance().itemColors.register(colorBlock.itemColor, block.get())
            }
        }
        LogManager.log(this, PhoenixRecipeBookCategories.OVEN.name)
    }
}