package phoenix.init.events

import com.google.common.collect.ImmutableSet
import net.minecraft.block.Block
import net.minecraft.block.FlowingFluidBlock
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.biome.Biomes
import net.minecraftforge.event.RegistryEvent.Register
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import phoenix.Phoenix
import phoenix.Phoenix.Companion.PHOENIX
import phoenix.init.PhoenixBiomes.HEARTVOID
import phoenix.init.PhoenixBiomes.UNDER
import phoenix.init.PhoenixBlocks.BLOCKS
import phoenix.init.PhoenixEntities.CAUDA
import phoenix.init.PhoenixEntities.TALPA
import phoenix.init.PhoenixFeatures
import phoenix.init.PhoenixRecipes
import phoenix.network.NetworkHandler
import phoenix.utils.block.INonItem
import java.util.function.Predicate

@EventBusSubscriber(modid = Phoenix.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object PhoenixCommonEvents
{
    @SubscribeEvent
    @JvmStatic
    fun onRegisterItems(event: Register<Item>)
    {
        val registry = event.registry
        BLOCKS.entries.stream()
                .map { obj: RegistryObject<Block> -> obj.get() }
                .filter { block: Block -> block !is INonItem }
                .filter { block: Block -> block !is FlowingFluidBlock }
                .forEach { block: Block ->
                    val prop = Item.Properties().group(PHOENIX)
                    val blockItem = BlockItem(block, prop)
                    blockItem.registryName = block.registryName
                    registry.register(blockItem)
                }
    }

    @SubscribeEvent
    @JvmStatic
    fun init(event: FMLCommonSetupEvent)
    {
        NetworkHandler.init()
        FMLJavaModLoadingContext.get().modEventBus.register(PhoenixCommonEvents::class.java)
        PhoenixRecipes.register()
        StructureHelper.addStructure(Biomes.END_HIGHLANDS, PhoenixFeatures.ERASED.get())
        StructureHelper.addStructure(HEARTVOID.get(), PhoenixFeatures.ERASED.get())
        addEntityToBiome(endBiomes, EntityClassification.CREATURE, TALPA.get(), 10, 1..3)
        addEntityToBiome(endBiomes, EntityClassification.CREATURE, CAUDA.get(), 10, 1..3)
        
        for (biome in Registry.BIOME)
            if (!endBiomes.contains(biome))
                StructureHelper.addZirconiumOre(biome)
    }

    fun <T : Entity> addEntityToBiome(biome : Biome, classification : EntityClassification, entity : EntityType<T>, weight : Int, b : Int, c : Int)
    {
        biome.addSpawn(classification, SpawnListEntry(entity, weight, b, c))
    }

    private fun <T : Entity> addEntityToBiome(biome : Biome, classification : EntityClassification, entity : EntityType<T>, weight : Int, count : IntRange)
    {
        biome.addSpawn(classification, SpawnListEntry(entity, weight, count.first, count.last))
    }

    private fun <T : Entity> addEntityToBiome(biomes : Collection<Biome>, classification : EntityClassification, entity : EntityType<T>, weight : Int, count : IntRange)
    {
        for(biome in biomes)
            biome.addSpawn(classification, SpawnListEntry(entity, weight, count.first, count.last))
    }

    private val endBiomes : ImmutableSet<Biome> = ImmutableSet.of(Biomes.END_HIGHLANDS, Biomes.END_MIDLANDS, HEARTVOID.get(), UNDER.get(), Biomes.SMALL_END_ISLANDS, Biomes.THE_END, Biomes.END_BARRENS)
}

private operator fun Biome.component1(): Biome
{
    return this
}
