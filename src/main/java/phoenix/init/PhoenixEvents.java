package phoenix.init;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import phoenix.Phoenix;
import phoenix.client.render.PipeRender;
import phoenix.client.render.TalpaRenderer;
import phoenix.client.render.TankRenderer;

@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PhoenixEvents
{
    @SubscribeEvent
    public static void onJoin(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof PlayerEntity)
        {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            player.sendMessage(new StringTextComponent("Hello, %p!".replace("%p", player.getName().getFormattedText())));
        }
    }

    @SubscribeEvent
    public static void preInit(FMLCommonSetupEvent event)
    {
        PhoenixBiomes.UNDER.get().addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(PhoenixEntities.TALPA.get(), 10, 2, 8));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.TANK.get(), RenderType.getCutoutMipped());
        RenderingRegistry.registerEntityRenderingHandler(PhoenixEntities.TALPA.get(), TalpaRenderer::new);
        PhoenixContainers.renderScreens();
        DistExecutor.runWhenOn(Dist.CLIENT, ()->()->ClientRegistry.bindTileEntityRenderer(PhoenixTiles.PIPE.get(),   PipeRender::new));
        DistExecutor.runWhenOn(Dist.CLIENT, ()->()->ClientRegistry.bindTileEntityRenderer(PhoenixTiles.TANK.get(), TankRenderer::new));
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event)
    {
        PhoenixBiomes.UNDER    .get().addStructure(PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        PhoenixBiomes.HEARTVOID.get().addStructure(PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        PhoenixBiomes.UNDER    .get().addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
        PhoenixBiomes.HEARTVOID.get().addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));

        addStructure(Biomes.END_HIGHLANDS, PhoenixFeatures.ERASED.get());
    }

    public static void addStructure(   Biome biome,    Structure structure)
    {
        biome.addStructure(structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, structure
                                                                            .withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG)
                                                                            .withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    }
}
