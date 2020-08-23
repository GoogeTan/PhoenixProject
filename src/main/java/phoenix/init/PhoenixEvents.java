package phoenix.init;

import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import phoenix.Phoenix;
import phoenix.client.render.PipeRender;
import phoenix.client.render.TalpaRenderer;
import phoenix.client.render.TankRenderer;
import phoenix.world.capa.IStager;
import phoenix.world.capa.StageHandler;
import phoenix.world.capa.StageProvider;
import phoenix.world.capa.StageStorage;

import static net.minecraftforge.common.BiomeDictionary.Type.END;

@Mod.EventBusSubscriber(modid = Phoenix.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PhoenixEvents
{
    @SubscribeEvent
    public static void setCapability(AttachCapabilitiesEvent<World> event)
    {
        event.addCapability(new ResourceLocation(Phoenix.MOD_ID, "phoenix"), new StageProvider());
    }

    @SubscribeEvent
    public static void preInit(FMLCommonSetupEvent evt)
    {
        CapabilityManager.INSTANCE.register(IStager.class, new StageStorage(), StageHandler::new);//reg capablity
        registerBiome(PhoenixBiomes.UNDER.get(), 5, BiomeManager.BiomeType.COOL, END);
        DistExecutor.runWhenOn(Dist.CLIENT, ()->()->ClientRegistry.bindTileEntityRenderer(PhoenixTile.PIPE.get(),   PipeRender::new));
        DistExecutor.runWhenOn(Dist.CLIENT, ()->()->ClientRegistry.bindTileEntityRenderer(PhoenixTile.TANK.get(), TankRenderer::new));
    }

    //@SubscribeEvent
    public static void om(TextureStitchEvent event)
    {
        Minecraft.getInstance().textureManager.loadAsync(
                new ResourceLocation(Phoenix.MOD_ID, "textures/blocks/chorus_plant.png"),
                Util.getServerExecutor());
        Minecraft.getInstance().textureManager.loadTexture(
                new ResourceLocation(Phoenix.MOD_ID, "textures/blocks/chorus_plant.png"),
                new AtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
    }

    public static void registerBiome(Biome biome, int weight, BiomeManager.BiomeType type, BiomeDictionary.Type... types)
    {
        BiomeDictionary.addTypes(biome, types);
        BiomeManager.addBiome(type, new BiomeManager.BiomeEntry(biome, weight));
        BiomeManager.addSpawnBiome(biome);
        BiomeProvider.BIOMES_TO_SPAWN_IN.add(biome);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
        RenderTypeLookup.setRenderLayer(PhoenixBlocks.TANK.get(), RenderType.getCutoutMipped());
        RenderingRegistry.registerEntityRenderingHandler(PhoenixEntities.TALPA.get(), TalpaRenderer::new);
        Phoenix.LOGGER.error("ТАКИ ВЫЗВАЛСЯ!");
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event)
    {
        PhoenixBiomes.UNDER.get().addStructure(PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        Biomes.END_HIGHLANDS.addStructure(PhoenixFeatures.ERASED.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
    }
}
