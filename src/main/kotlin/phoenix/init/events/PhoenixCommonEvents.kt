package phoenix.init.events

import net.minecraft.block.Block
import net.minecraft.block.FlowingFluidBlock
import net.minecraft.entity.EntityClassification
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.item.crafting.Ingredient
import net.minecraft.potion.PotionBrewing
import net.minecraft.potion.PotionUtils
import net.minecraft.potion.Potions
import net.minecraft.potion.Potions.MUNDANE
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.biome.Biomes.*
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraftforge.common.brewing.BrewingRecipeRegistry
import net.minecraftforge.common.brewing.IBrewingRecipe
import net.minecraftforge.event.RegistryEvent.Register
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import phoenix.Phoenix
import phoenix.Phoenix.Companion.ASH
import phoenix.init.PhoenixBiomes.HEARTVOID
import phoenix.init.PhoenixBiomes.UNDER
import phoenix.init.PhoenixBlocks.BLOCKS
import phoenix.init.PhoenixEntities.CAUDA
import phoenix.init.PhoenixEntities.TALPA
import phoenix.init.PhoenixFeatures
import phoenix.init.PhoenixItems
import phoenix.init.PhoenixPotions
import phoenix.init.PhoenixPotions.LEVITATION
import phoenix.init.PhoenixPotions.LONG_LEVITATION
import phoenix.init.PhoenixRecipes
import phoenix.network.NetworkHandler
import phoenix.utils.addStructure
import phoenix.utils.addZirconiumOre
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
        UNDER.addSpawn(EntityClassification.CREATURE, SpawnListEntry(CAUDA, 15, 1, 1))
        HEARTVOID.addSpawn(EntityClassification.CREATURE, SpawnListEntry(TALPA, 15, 1, 4))
        END_HIGHLANDS.addStructure(PhoenixFeatures.REMAINS)
        UNDER.addStructure(PhoenixFeatures.REMAINS)
        HEARTVOID.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, PhoenixFeatures.WET_TREE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG))

        for (biome in Registry.BIOME)
        {
            if (biome !== END_BARRENS && biome !== END_HIGHLANDS && biome !== END_MIDLANDS && biome !== THE_END &&
                    biome !== SMALL_END_ISLANDS && biome !== UNDER && biome !== HEARTVOID && biome !== THE_VOID && biome !== NETHER)
            {
                biome.addZirconiumOre()
            }
        }

        BrewingRecipeRegistry.addRecipe(
            Ingredient.fromStacks(PotionUtils.addPotionToItemStack(ItemStack(Items.POTION), MUNDANE)),
            Ingredient.fromItems(PhoenixItems.GOLDEN_SETA),
            PotionUtils.addPotionToItemStack(ItemStack(Items.POTION), LEVITATION))

        BrewingRecipeRegistry.addRecipe(
            Ingredient.fromStacks(PotionUtils.addPotionToItemStack(ItemStack(Items.POTION), LEVITATION)),
            Ingredient.fromItems(Items.REDSTONE),
            PotionUtils.addPotionToItemStack(ItemStack(Items.POTION), LONG_LEVITATION))
    }
}