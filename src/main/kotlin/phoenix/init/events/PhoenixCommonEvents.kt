package phoenix.init.events

import net.minecraft.block.Block
import net.minecraft.block.FlowingFluidBlock
import net.minecraft.entity.EntityClassification
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.biome.Biomes.*
import net.minecraftforge.event.RegistryEvent.Register
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import phoenix.Phoenix
import phoenix.Phoenix.Companion.ASH
import phoenix.init.PhoenixBiomes.HEARTVOID
import phoenix.init.PhoenixBiomes.UNDER
import phoenix.init.PhoenixBlocks.BLOCKS
import phoenix.init.PhoenixEntities.TALPA
import phoenix.init.PhoenixFeatures
import phoenix.init.PhoenixRecipes
import phoenix.utils.addStructure
import phoenix.utils.addZirconiumOre
import phoenix.network.NetworkHandler
import phoenix.utils.block.ICustomGroup
import phoenix.utils.block.INonItem
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate

@EventBusSubscriber(modid = Phoenix.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object PhoenixCommonEvents
{
    @SubscribeEvent
    fun onRegisterItems(event: Register<Item>)
    {
        BLOCKS.getEntries().stream()
                .map(ObjectHolderDelegate<out Block>::get)
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
    fun init(event: FMLCommonSetupEvent)
    {
        NetworkHandler.init()
        PhoenixRecipes.register()
        //UNDER    .get().addSpawn(EntityClassification.CREATURE, SpawnListEntry(CAUDA, 15, 1, 3))
        HEARTVOID.addSpawn(EntityClassification.CREATURE, SpawnListEntry(TALPA, 15, 1, 4))
        END_HIGHLANDS.addStructure(PhoenixFeatures.REMAINS)
        HEARTVOID.addStructure(PhoenixFeatures.REMAINS)
        UNDER.addStructure(PhoenixFeatures.REMAINS)

        for (biome in Registry.BIOME)
        {
            if (biome !== END_BARRENS && biome !== END_HIGHLANDS && biome !== END_MIDLANDS && biome !== THE_END &&
                    biome !== SMALL_END_ISLANDS && biome !== UNDER && biome !== HEARTVOID && biome !== THE_VOID && biome !== NETHER)
            {
                biome.addZirconiumOre()
            }
        }
    }
    /*
    @SubscribeEvent
    fun attachEnergy(event : AttachCapabilitiesEvent<TileEntity>)
    {
        val value = event.`object`
        if(value is PipeTile)
        {
            event.addCapability(ResourseUtils.key("energy"), EnergyStorageProvider())
        }
    }
    */
}