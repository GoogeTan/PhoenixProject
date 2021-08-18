package phoenix.init.events

import net.minecraft.block.Block
import net.minecraft.block.FlowingFluidBlock
import net.minecraft.block.material.MaterialColor
import net.minecraft.entity.EntityClassification
import net.minecraft.item.*
import net.minecraft.item.crafting.Ingredient
import net.minecraft.potion.PotionUtils
import net.minecraft.potion.Potions.MUNDANE
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome.SpawnListEntry
import net.minecraft.world.biome.Biomes.*
import net.minecraft.world.gen.GenerationStage
import net.minecraft.world.gen.feature.IFeatureConfig
import net.minecraftforge.common.brewing.BrewingRecipeRegistry
import net.minecraftforge.event.RegistryEvent.Register
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.registries.ForgeRegistries
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.Phoenix.Companion.ASH
import phoenix.api.block.ICustomGroup
import phoenix.api.block.INonItem
import phoenix.init.PhxBiomes.HEARTVOID
import phoenix.init.PhxBiomes.UNDER
import phoenix.init.PhxBlocks.blocks
import phoenix.init.PhxEntities
import phoenix.init.PhxEntities.cauda
import phoenix.init.PhxEntities.talpa
import phoenix.init.PhxFeatures
import phoenix.init.PhxItems
import phoenix.init.PhxPotions.LEVITATION
import phoenix.init.PhxPotions.LONG_LEVITATION
import phoenix.init.PhxRecipes
import phoenix.items.FixedSpawnEggItem
import phoenix.network.initPacketSystem
import phoenix.other.addStructure
import phoenix.other.addZirconiumOre
import phoenix.utils.ServerStageUppedEvent
import thedarkcolour.kotlinforforge.forge.ObjectHolderDelegate

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object PhoenixCommonEvents
{
    @SubscribeEvent
    fun onRegisterItems(event: Register<Item>)
    {
        blocks.getEntries().stream()
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
        initPacketSystem()
        PhxRecipes.register()
        UNDER.addSpawn(EntityClassification.CREATURE, SpawnListEntry(cauda, 15, 1, 1))
        HEARTVOID.addSpawn(EntityClassification.CREATURE, SpawnListEntry(talpa, 15, 1, 4))
        END_HIGHLANDS.addStructure(PhxFeatures.REMAINS)
        UNDER.addStructure(PhxFeatures.REMAINS)
        UNDER.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, PhxFeatures.WATER.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG))
        HEARTVOID.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, PhxFeatures.WET_TREE.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG))

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
            Ingredient.fromItems(PhxItems.GOLDEN_SETA),
            PotionUtils.addPotionToItemStack(ItemStack(Items.POTION), LEVITATION)
        )

        BrewingRecipeRegistry.addRecipe(
            Ingredient.fromStacks(PotionUtils.addPotionToItemStack(ItemStack(Items.POTION), LEVITATION)),
            Ingredient.fromItems(Items.REDSTONE),
            PotionUtils.addPotionToItemStack(ItemStack(Items.POTION), LONG_LEVITATION)
        )
    }

    @SubscribeEvent
    fun registerItems(event : Register<Item>)
    {
        if (event.registry === ForgeRegistries.ITEMS)
        {
            val caudaEgg = FixedSpawnEggItem(PhxEntities::cauda, MaterialColor.BLACK.colorValue, MaterialColor.BLACK_TERRACOTTA.colorValue, Item.Properties().group(ItemGroup.MISC))
            caudaEgg.registryName = ResourceLocation(MOD_ID, "cauda_spawn_egg")
            val golemSpawnEntity = FixedSpawnEggItem(PhxEntities::ancientGolemEntity, MaterialColor.CYAN.colorValue, MaterialColor.BLACK.colorValue, Item.Properties().group(ItemGroup.MISC))
            golemSpawnEntity.registryName = ResourceLocation(MOD_ID, "ancient_golem_spawn_egg")
            ForgeRegistries.ITEMS.registerAll(caudaEgg, golemSpawnEntity)
        }
    }

    @SubscribeEvent
    fun onStageUppded(event : ServerStageUppedEvent)
    {
    }
}