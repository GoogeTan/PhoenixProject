package phoenix.init.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;
import phoenix.Phoenix;
import phoenix.utils.Truple;
import phoenix.world.GenSaveData;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class PhoenixEventsOther
{
    public static ArrayList<Truple<Integer, Integer, Runnable>> tasks = new ArrayList<>();

    @SubscribeEvent
    public static void deferredTasks(TickEvent.WorldTickEvent event)
    {
        if(!event.world.isRemote)
        {
            if(!tasks.isEmpty())
            if (event.phase == TickEvent.Phase.END)
                for (int i = 0; i < tasks.size(); ++i)
                {
                    Truple<Integer, Integer, Runnable> current = tasks.get(i);
                    current.first++;
                    if (current.first >= current.second)
                    {
                        current.third.run();
                        tasks.remove(i);
                        i--;
                    }
                }
        }
    }

    public static void addTask(int time, Runnable r)
    {
        tasks.add(new Truple<>(0, time, r));
    }

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
    }

    @SubscribeEvent
    public static void onOpenGui(GuiOpenEvent event)
    {
        if(event.getGui() instanceof MainMenuScreen)
        {
            Minecraft.getInstance().splashes.possibleSplashes.add("God is an artist, since there are so many colors in the world");
            Minecraft.getInstance().splashes.possibleSplashes.add("The essence of life is that it changes itself");
            Minecraft.getInstance().splashes.possibleSplashes.add("Project E.N.D.");
            Minecraft.getInstance().splashes.possibleSplashes.add("Third child is ann angel!!");
            Minecraft.getInstance().splashes.possibleSplashes.add("Sixth station is absent");
        }
    }
}