package projectend.world;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import projectend.world.biomes.BiomeEndUnder;

public class BiomeRegistrar 
{
	public static final Biome END_UNDER = new BiomeEndUnder();
	
	public static void registerBiomes()
	{
		initBiome(END_UNDER,"under", Type.END);
	}
	
	private static void initBiome(Biome biome, String name, Type... types)
	{
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
		BiomeDictionary.addTypes(biome, types);
	}
}
