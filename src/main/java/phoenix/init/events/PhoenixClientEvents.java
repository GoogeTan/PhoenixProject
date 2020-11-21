package phoenix.init.events;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import phoenix.Phoenix;
import phoenix.client.render.PipeRender;
import phoenix.client.render.TankRenderer;
import phoenix.client.render.TextRenderer;
import phoenix.client.render.entity.CaudaRenderer;
import phoenix.client.render.entity.KnifeRenderer;
import phoenix.client.render.entity.TalpaRenderer;
import phoenix.init.*;
import phoenix.utils.block.IColoredBlock;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PhoenixClientEvents
{
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        PhoenixRenderTypes.init();
        PhoenixKeyBindings.register();
        PhoenixContainers.registerScreens();

        RenderingRegistry.registerEntityRenderingHandler(PhoenixEntities.getTALPA().get(), TalpaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(PhoenixEntities.getCAUDA().get(), CaudaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(PhoenixEntities.getKNIFE().get(), KnifeRenderer::new);

        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.getPIPE().get(), PipeRender::new);
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.getTANK().get(), TankRenderer::new);
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.getTEXT().get(), TextRenderer::new);

        // регистрация цветных блоков
        for (RegistryObject<Block> block : PhoenixBlocks.getBLOCKS().getEntries())
        {
            if (block.get() instanceof IColoredBlock)
            {
                IColoredBlock colorBlock = (IColoredBlock) block.get();
                if (colorBlock.getBlockColor() != null)
                    Minecraft.getInstance().getBlockColors().register(colorBlock.getBlockColor(), block.get());
                if (colorBlock.getItemColor() != null)
                    Minecraft.getInstance().getItemColors().register(colorBlock.getItemColor(), Item.getItemFromBlock(block.get()));
            }
        }
    }
}