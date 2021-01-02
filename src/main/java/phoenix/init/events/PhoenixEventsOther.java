package phoenix.init.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.util.Splashes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import phoenix.Phoenix;
import phoenix.utils.IChapterReader;
import phoenix.network.NetworkHandler;
import phoenix.network.SyncBookPacket;
import phoenix.utils.StringUtils;
import phoenix.world.GenSaveData;

@Mod.EventBusSubscriber
public class PhoenixEventsOther
{
    @SubscribeEvent
    public static void cornGen(EntityJoinWorldEvent event)
    {
        World world = event.getWorld();
        if(!world.isRemote && world.dimension.getType() == DimensionType.THE_END && !GenSaveData.get((ServerWorld) world).isCornGenned())
        {
            TemplateManager manager = ((ServerWorld) world).getStructureTemplateManager();
            Template template = manager.getTemplate(new ResourceLocation("phoenix:corn/corn"));
            if(template != null)
            {
                GenSaveData.get((ServerWorld) world).setCornGenned();
                template.addBlocksToWorld(world, new BlockPos(1000, 100, 1000), new PlacementSettings());
                template.addBlocksToWorld(world, new BlockPos(-1000, 100, 1000), new PlacementSettings());
                template.addBlocksToWorld(world, new BlockPos(1000, 100, -1000), new PlacementSettings());
                template.addBlocksToWorld(world, new BlockPos(-1000, 100, -1000), new PlacementSettings());
                Phoenix.getLOGGER().log(Level.DEBUG,"Corn genned ^)");
            }
            else
            {
                Phoenix.getLOGGER().error("Corn was not genned ^(. template is null... I think it is bad.");
            }
        }
        if(!event.getWorld().isRemote && event.getEntity() instanceof ServerPlayerEntity)
        {
            NetworkHandler.INSTANCE.sendTo(new SyncBookPacket(((IChapterReader)event.getEntity()).getOpenedChapters()), (ServerPlayerEntity) event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onOpenGui(GuiOpenEvent event)
    {
        if(event.getGui() instanceof MainMenuScreen)
        {
            Splashes splashes = Minecraft.getInstance().splashes;
            splashes.possibleSplashes.add(StringUtils.rainbowColor("God is an artist, since there are so many \n colors in the world"));
            splashes.possibleSplashes.add(TextFormatting.RED + "The essence of life is that it changes itself");
            splashes.possibleSplashes.add(TextFormatting.BLUE + "Bridge station is absent");
            splashes.possibleSplashes.add(TextFormatting.DARK_BLUE + "Third child is ann angel!!");
            splashes.possibleSplashes.add(TextFormatting.BLACK + "Project E.N.D.");
            splashes.possibleSplashes.add(TextFormatting.BLACK + "Нож в печень, FX вечен!");
        }
    }
}