package projectend.init;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import projectend.ProjectEnd;

@Config(modid = ProjectEnd.MOD_ID, category = "")
@Mod.EventBusSubscriber(modid = ProjectEnd.MOD_ID)
public class Configs 
{
	public static ConfigWorldGen worldgen = new ConfigWorldGen();
	
	public static class ConfigWorldGen 
	{
		@Config.Comment({"Controls size of end biomes. Larger number = larger biomes", "Default: 4"})
		@Config.RequiresWorldRestart
		public int endBiomeSize = 7;
		
		@Config.Comment({"Reduce number of end biomes by percent (range 0-99). e.g. 40 would generate 40% fewer end biomes", "Default: 0"})
		@Config.RequiresWorldRestart
		public int biomeReducer = 0;

		@Config.Comment({"Game mode. \"Basic\" for normal, \"Liahim\" for hard mode", "Default: Basic"})
		@Config.RequiresWorldRestart
		public String mode = "Basic";
	}
	
	@SubscribeEvent
	public static void onConfigReload(ConfigChangedEvent.OnConfigChangedEvent event) 
	{
		if (ProjectEnd.MOD_ID.equals(event.getModID()))
			ConfigManager.sync(ProjectEnd.MOD_ID, Config.Type.INSTANCE);
	}

}
