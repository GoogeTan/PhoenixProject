package phoenix.init;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityClassification;
import net.minecraft.item.Item;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import phoenix.Phoenix;
import phoenix.client.render.PipeRender;
import phoenix.client.render.TankRenderer;
import phoenix.client.render.entity.CaudaRenderer;
import phoenix.client.render.entity.TalpaRenderer;
import phoenix.utils.IColoredBlock;
import phoenix.world.GenSaveData;

@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PhoenixEvents
{

    @SubscribeEvent
    public static void preInit(FMLCommonSetupEvent event)
    {
        PhoenixBiomes.UNDER.get().addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(PhoenixEntities.TALPA.get(), 10, 2, 8));
    }

    @SubscribeEvent
    public static void cornGen(EntityJoinWorldEvent event)
    {
        World world = event.getWorld();
        if(!world.isRemote && world.dimension.getType() == DimensionType.THE_END && !GenSaveData.get((ServerWorld) world).isCornGened())
        {
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(Rotation.randomRotation(world.getRandom()))
                    .setMirror(Mirror.NONE).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);

        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.TANK.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.OVEN.get(), RenderType.getCutoutMipped());

        RenderingRegistry.registerEntityRenderingHandler(PhoenixEntities.TALPA.get(), TalpaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(PhoenixEntities.CAUDA.get(), CaudaRenderer::new);

        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.PIPE.get(),   PipeRender::new);
        ClientRegistry.bindTileEntityRenderer(PhoenixTiles.TANK.get(), TankRenderer::new);

        PhoenixContainers.renderScreens();
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

    @OnlyIn(Dist.DEDICATED_SERVER)
    @SubscribeEvent
    public static void join(EntityJoinWorldEvent event)
    {
        if(!GenSaveData.get((ServerWorld) event.getWorld()).isCornGened() && event.getWorld().dimension.getType() == DimensionType.THE_END)
        {
            GenSaveData.get((ServerWorld) event.getWorld()).setCornGened();

        }
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event)
    {
        PhoenixBiomes.UNDER    .get().addStructure(PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        PhoenixBiomes.HEARTVOID.get().addStructure(PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        PhoenixBiomes.UNDER    .get().addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        PhoenixBiomes.HEARTVOID.get().addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));

        PhoenixBiomes.UNDER    .get().addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(PhoenixEntities.CAUDA.get(), 15, 1, 3));
        PhoenixBiomes.HEARTVOID.get().addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(PhoenixEntities.TALPA.get(), 15, 1, 4));

        addStructure(Biomes.END_HIGHLANDS, PhoenixFeatures.ERASED.get());
    }

    public static void addStructure(   Biome biome,    Structure structure)
    {
        biome.addStructure(structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                                                                                 .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    }
}
