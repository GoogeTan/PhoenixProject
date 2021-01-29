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
import phoenix.network.NetworkHandler;
import phoenix.network.SyncBookPacket;
import phoenix.utils.IChapterReader;
import phoenix.utils.LogManager;
import phoenix.utils.StringUtils;
import phoenix.world.GenSaveData;

import java.util.ArrayList;

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
                LogManager.log("<Other events> ", "Corn genned ^)");
            }
            else
            {
                LogManager.error("<Other events> ", "Corn was not genned ^(. template is null... I it is very bad think.");
            }
        }
        if(!event.getWorld().isRemote && event.getEntity() instanceof ServerPlayerEntity && event.getEntity() instanceof IChapterReader)
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

            splashes.possibleSplashes.add(StringUtils.rainbowColor("God is an artist, since there are so many \n colors in the world"));//Reference to: Beautiful mind
            splashes.possibleSplashes.add(TextFormatting.RED   + "The essence of life is that it changes itself");//Reference to: Evangelion-3.33 you can(not) redo
            splashes.possibleSplashes.add(TextFormatting.BLUE  + "Bridge station is absent");//Reference to: Dovecote in a yellow glade
            splashes.possibleSplashes.add(TextFormatting.GRAY  + "You can be wind... be forever."); //Reference to: Dovecote in a yellow glade
            splashes.possibleSplashes.add(TextFormatting.DARK_BLUE + "Third child is ann angel!!");//Reference to: Neon Genesis Evangelion
            splashes.possibleSplashes.add(TextFormatting.GOLD  + "Project E.N.D."); // Reference to: Phoenix project's old name
            splashes.possibleSplashes.add(TextFormatting.BLACK + "Нож в печень, FX вечен!"); // Reference to: AMD FX series
            splashes.possibleSplashes.add(TextFormatting.AQUA  + "Still, the first enemy of human is itself."); // Reference to: Neon Genesis Evangelion
            splashes.possibleSplashes.add(TextFormatting.WHITE + "The hands of the clock cannot be turned back. \n" + TextFormatting.WHITE + "But it is in our power to move them forward!"); // Reference to: Neon Genesis Evangelion
            splashes.possibleSplashes.add(TextFormatting.RED   + "Where are the fixes, Lebowski?"); // Reference to: The Big Lebowski
        }
    }
}