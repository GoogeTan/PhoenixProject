package projectend.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import projectend.ProjectEnd;
import projectend.world.capablity.StageProvider;

@Mod.EventBusSubscriber
public class ProjectENDEventHandler
{
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<World> event)
    {
        event.addCapability(new ResourceLocation(ProjectEnd.MOD_ID, ProjectEnd.MOD_NAME), new StageProvider());
    }
}
