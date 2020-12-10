package phoenix.init.events

import net.minecraft.block.Block
import net.minecraft.block.FlowingFluidBlock
import net.minecraft.entity.EntityClassification
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.biome.Biomes
import net.minecraftforge.event.RegistryEvent.Register
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import phoenix.Phoenix
import phoenix.Phoenix.Companion.ASH
import phoenix.init.PhoenixBiomes.HEARTVOID
import phoenix.init.PhoenixBiomes.UNDER
import phoenix.init.PhoenixBlocks.BLOCKS
import phoenix.init.PhoenixEntities.CAUDA
import phoenix.init.PhoenixEntities.TALPA
import phoenix.init.PhoenixFeatures
import phoenix.init.PhoenixRecipes
import phoenix.network.NetworkHandler
import phoenix.utils.block.ICustomGroup
import phoenix.utils.block.INonItem

@EventBusSubscriber(modid = Phoenix.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object PhoenixCommonEvents
{
    @SubscribeEvent
    @JvmStatic
    fun onRegisterItems(event: Register<Item>)
    {
        BLOCKS.entries.stream()
                .map { obj: RegistryObject<Block> -> obj.get() }
                .filter { block: Block -> block !is INonItem }
                .filter { block: Block -> block !is FlowingFluidBlock }
                .forEach { block: Block ->
                    val tab = if(block is ICustomGroup) block.tab else ASH
                    val prop = Item.Properties().group(tab)
                    val blockItem = BlockItem(block, prop)
                    blockItem.registryName = block.registryName
                    event.registry.register(blockItem)
                }
    }

    @SubscribeEvent
    @JvmStatic
    fun init(event: FMLCommonSetupEvent)
    {
        //Feature.END_SPIKE = Phoenix.customEndSpike
        //EndSpikeFeature.LOADING_CACHE = CacheBuilder.newBuilder().expireAfterWrite(5L, TimeUnit.MINUTES).build(CustomEndSpike.EndSpikeCacheLoader())

        NetworkHandler.init()
        FMLJavaModLoadingContext.get().modEventBus.register(PhoenixCommonEvents::class.java)
        PhoenixRecipes.register()
        UNDER    .get().addSpawn(EntityClassification.CREATURE, SpawnListEntry(CAUDA.get(), 15, 1, 3))
        HEARTVOID.get().addSpawn(EntityClassification.CREATURE, SpawnListEntry(TALPA.get(), 15, 1, 4))
        StructureHelper.addStructure(Biomes.END_HIGHLANDS, PhoenixFeatures.REMAINS.get())
        StructureHelper.addStructure(HEARTVOID.get(), PhoenixFeatures.REMAINS.get())
        StructureHelper.addStructure(UNDER.get(), PhoenixFeatures.REMAINS.get())
        for (biome in Registry.BIOME)
        {
            if (biome !== Biomes.END_BARRENS && biome !== Biomes.END_HIGHLANDS && biome !== Biomes.END_MIDLANDS && biome !== Biomes.THE_END && biome !== Biomes.SMALL_END_ISLANDS && biome !== UNDER.get() && biome !== HEARTVOID.get())
            {
                StructureHelper.addZirconiumOre(biome)
            }
        }
    }

}