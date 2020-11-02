package phoenix.init;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityClassification;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import phoenix.Phoenix;
import phoenix.client.render.PipeRender;
import phoenix.client.render.TankRenderer;
import phoenix.client.render.entity.CaudaRenderer;
import phoenix.client.render.entity.TalpaRenderer;
import phoenix.utils.IColoredBlock;
import phoenix.utils.INonItem;
import phoenix.world.GenSaveData;

@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PhoenixEvents
{
    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        final IForgeRegistry<Item> registry = event.getRegistry();
        PhoenixBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                .filter(block -> !(block instanceof INonItem))
                .filter(block -> !(block instanceof FlowingFluidBlock))
                .forEach(block ->
                {
                    final Item.Properties prop = new Item.Properties().group(Phoenix.PHOENIX);
                    final BlockItem blockItem = new BlockItem(block, prop);
                    blockItem.setRegistryName(block.getRegistryName());
                    registry.register(blockItem);
                });
    }

    @SubscribeEvent
    public static void cornGen(EntityJoinWorldEvent event)
    {
        Phoenix.LOGGER.error("joined!!!");

        World world = event.getWorld();
        if(!world.isRemote && world.dimension.getType() == DimensionType.THE_END && !GenSaveData.get((ServerWorld) world).isCornGenned())
        {

            TemplateManager manager = ((ServerWorld) world).getStructureTemplateManager();
            manager.getTemplate(new ResourceLocation("phoenix:corn/corn"))
                    .addBlocksToWorld(world, new BlockPos(1000, 100, 1000), new PlacementSettings());
            manager.getTemplate(new ResourceLocation("phoenix:corn/corn"))
                    .addBlocksToWorld(world, new BlockPos(-1000, 100, 1000), new PlacementSettings());
            manager.getTemplate(new ResourceLocation("phoenix:corn/corn"))
                    .addBlocksToWorld(world, new BlockPos(1000, 100, -1000), new PlacementSettings());
            manager.getTemplate(new ResourceLocation("phoenix:corn/corn"))
                    .addBlocksToWorld(world, new BlockPos(-1000, 100, -1000), new PlacementSettings());

            Phoenix.LOGGER.error("Corn genned");
            GenSaveData.get((ServerWorld) world).setCornGenned();
        }
        else
        {
            Phoenix.LOGGER.error(!world.isRemote + " " + world.dimension.getType() + " " + GenSaveData.get((ServerWorld) world).isCornGenned());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        PhoenixKeyBindings.register();

        RenderTypeLookup.setRenderLayer(PhoenixBlocks.TANK.get(), RenderType.getCutoutMipped());

        RenderingRegistry.registerEntityRenderingHandler(PhoenixEntities.TALPA.get(), TalpaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(PhoenixEntities.CAUDA.get(), CaudaRenderer::new);

        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.PIPE.get(),   PipeRender::new);
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.TANK.get(), TankRenderer::new);

        PhoenixContainers.registerScreens();

        // регистрация цветных блоков
        for(RegistryObject<Block> block : PhoenixBlocks.BLOCKS.getEntries())
        {
            if(block.get() instanceof IColoredBlock)
            {
                IColoredBlock colorBlock = (IColoredBlock)block.get();
                if (colorBlock.getBlockColor() != null)
                    Minecraft.getInstance().getBlockColors().register(colorBlock.getBlockColor(), block.get());
                if (colorBlock.getItemColor() != null)
                    Minecraft.getInstance().getItemColors().register(colorBlock.getItemColor(), Item.getItemFromBlock(block.get()));
            }
        }
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event)
    {
        PhoenixRecipes.register();

        PhoenixBiomes.UNDER    .get().addStructure(PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        PhoenixBiomes.HEARTVOID.get().addStructure(PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        PhoenixBiomes.UNDER    .get().addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        PhoenixBiomes.HEARTVOID.get().addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));

        PhoenixBiomes.UNDER    .get().addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(PhoenixEntities.CAUDA.get(), 15, 1, 3));
        PhoenixBiomes.HEARTVOID.get().addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(PhoenixEntities.TALPA.get(), 15, 1, 4));

        addStructure(Biomes.END_HIGHLANDS, PhoenixFeatures.ERASED.get());


        for (Biome biome : Registry.BIOME)
        {
            if(biome != Biomes.END_BARRENS && biome != Biomes.END_HIGHLANDS && biome != Biomes.END_MIDLANDS && biome != Biomes.THE_END &&
            biome != Biomes.SMALL_END_ISLANDS && biome != PhoenixBiomes.UNDER.get() && biome != PhoenixBiomes.HEARTVOID.get())
            {
                addZirconiumOre(biome);
            }
        }
    }

    public static void addStructure(   Biome biome,    Structure structure)
    {
        biome.addStructure(structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                                                                                 .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    }


    public static void addZirconiumOre(Biome biome)
    {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, PhoenixBlocks.ZIRCONIUM.get().getDefaultState(), 4))
                        .withPlacement(Placement.COUNT_RANGE.configure(new CountRangeConfig(20, 0, 0, 64))));

    }
}
