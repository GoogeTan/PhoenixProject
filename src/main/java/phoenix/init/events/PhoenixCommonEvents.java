package phoenix.init.events;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.EntityClassification;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import phoenix.Phoenix;
import phoenix.init.*;
import phoenix.network.NetworkHandler;
import phoenix.utils.block.INonItem;

@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PhoenixCommonEvents
{
    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        final IForgeRegistry<Item> registry = event.getRegistry();
        PhoenixBlocks.getBLOCKS().getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> !(block instanceof INonItem))
                .filter(block -> !(block instanceof FlowingFluidBlock))
                .forEach(block ->
                {
                    final Item.Properties prop = new Item.Properties().group(Phoenix.Companion.getPHOENIX());
                    final BlockItem blockItem = new BlockItem(block, prop);
                    blockItem.setRegistryName(block.getRegistryName());
                    registry.register(blockItem);
                });
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event)
    {
        PhoenixSounds.init();
        NetworkHandler.init();
        FMLJavaModLoadingContext.get().getModEventBus().register(PhoenixCommonEvents.class);
        PhoenixRecipes.register();

        PhoenixBiomes.getUNDER()    .get().addStructure(PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        PhoenixBiomes.getHEARTVOID().get().addStructure(PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        PhoenixBiomes.getUNDER()    .get().addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        PhoenixBiomes.getHEARTVOID().get().addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));

        PhoenixBiomes.getUNDER()    .get().addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(PhoenixEntities.getCAUDA().get(), 15, 1, 3));
        PhoenixBiomes.getHEARTVOID().get().addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(PhoenixEntities.getTALPA().get(), 15, 1, 4));

        addStructure(Biomes.END_HIGHLANDS, PhoenixFeatures.ERASED.get());
        addStructure(Biomes.END_HIGHLANDS, PhoenixFeatures.CORN.get());

        for (Biome biome : Registry.BIOME)
        {
            if(biome != Biomes.END_BARRENS && biome != Biomes.END_HIGHLANDS && biome != Biomes.END_MIDLANDS && biome != Biomes.THE_END &&
                    biome != Biomes.SMALL_END_ISLANDS && biome != PhoenixBiomes.getUNDER().get() && biome != PhoenixBiomes.getHEARTVOID().get())
            {
                addZirconiumOre(biome);
            }
        }

        //Minecraft.getInstance().particles.registerFactory(((ParticleType)PhoenixParticles.INSTANCE.getPHOENIX_BORN().get()), new PhoenixBornFactory());
    }

    public static void addStructure(Biome biome, Structure structure)
    {
        biome.addStructure(structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    }

    public static void addZirconiumOre(Biome biome)
    {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, PhoenixBlocks.getZIRCONIUM().get().getDefaultState(), 4))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64))));
    }
}
