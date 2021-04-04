package phoenix.init

import net.minecraft.entity.Entity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.*
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.Phoenix.Companion.ASH
import phoenix.items.ItemDiary
import phoenix.items.ash.CrucibleItem
import phoenix.items.ash.KnifeItem
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhoenixItems
{
    private val ITEMS = KDeferredRegister(ForgeRegistries.ITEMS, Phoenix.MOD_ID)

    val GUIDE                          by ITEMS.register("diary", ::ItemDiary)

    val HIGH_QUALITY_CLAY_ITEM         by ITEMS.register("high_quality_clay", basicItem(Item.Properties().maxStackSize(16).group(ASH)))
    val COOKED_SETA                    by ITEMS.register("cooked_seta", basicFood(Food.Builder().hunger(3).saturation(1.0f).fastToEat().build(), ItemGroup.FOOD))!!
    val GOLDEN_SETA                    by ITEMS.register("golden_seta", basicFood(Food.Builder().hunger(5).saturation(1.5F).fastToEat().build(), ItemGroup.FOOD))!!

    val CRUCIBLE                       by ITEMS.register("crucible", basicItem())
    val CRUCIBLE_WITH_IRON_ORE         by ITEMS.register("crucible_with_iron_ore", ::CrucibleItem)
    val CRUCIBLE_WITH_IRON             by ITEMS.register("crucible_with_steel",     ::CrucibleItem)
    val CRUCIBLE_WITH_GOLD_ORE         by ITEMS.register("crucible_with_gold_ore", ::CrucibleItem)
    val CRUCIBLE_WITH_GOLD             by ITEMS.register("crucible_with_gold",     ::CrucibleItem)

    val IRON_ORE_WITH_COAL             by ITEMS.register("iron_ore_with_coal", basicItem())
    val IRON_WITH_COAL                 by ITEMS.register("iron_with_coal",     basicItem())
    val STEEL_INGOT                    by ITEMS.register("steel_ingot",        basicItem())
    val ZIRCONIUM_INGOT                by ITEMS.register("zirconium_ingot",    basicItem())

    val ZIRCONIUM_SWORD_BLADE          by ITEMS.register("zirconium_sword_blade", basicItem())
    val ZIRCONIUM_KNIFE_BLADE          by ITEMS.register("zirconium_knife_blade", basicItem())
    val ZIRCONIUM_BAYONET              by ITEMS.register("zirconium_pickaxe_head",basicItem())
    val ZIRCONIUM_AXE_BLADE            by ITEMS.register("zirconium_axe_blade",   basicItem())

    val STEEL_FORM_KNIFE_BLADE         by ITEMS.register("steel_form_knife_blade", basicItem())
    val STEEL_FORM_SWORD_BLADE         by ITEMS.register("steel_form_sword_blade", basicItem())
    val STEEl_FORM_PICKAXE_HEAD        by ITEMS.register("steel_form_pickaxe_head",basicItem())
    val STEEl_FORM_AXE_BLADE           by ITEMS.register("steel_form_axe_blade",   basicItem())

    val STEEL_FORM_KNIFE_BLADE_FULL    by ITEMS.register("steel_form_knife_blade_full", form { STEEL_FORM_KNIFE_BLADE })
    val STEEL_FORM_SWORD_BLADE_FULL    by ITEMS.register("steel_form_sword_blade_full", form { STEEL_FORM_SWORD_BLADE })
    val STEEl_FORM_PICKAXE_HEAD_FULL   by ITEMS.register("steel_form_pickaxe_head_full",form { STEEl_FORM_PICKAXE_HEAD })
    val STEEl_FORM_AXE_BLADE_FULL      by ITEMS.register("steel_form_axe_blade_full",   form { STEEl_FORM_AXE_BLADE })

    val STEEL_FORM_KNIFE_BLADE_ROASTED  by ITEMS.register("steel_form_knife_blade_roasted", form {STEEL_FORM_KNIFE_BLADE})
    val STEEL_FORM_SWORD_BLADE_ROASTED  by ITEMS.register("steel_form_sword_blade_roasted", form {STEEL_FORM_SWORD_BLADE})
    val STEEl_FORM_PICKAXE_HEAD_ROASTED by ITEMS.register("steel_form_pickaxe_head_roasted",form {STEEl_FORM_PICKAXE_HEAD})
    val STEEl_FORM_AXE_BLADE_ROASTED    by ITEMS.register("steel_form_axe_blade_roasted",   form {STEEl_FORM_AXE_BLADE})

    val ZIRCONIUM_AXE                  by ITEMS.register("ceramic_zirconium_axe")     { AxeItem    (PhoenixTiers.ZIRCONIUM_TIER, 9.0f, -3.2f, Item.Properties().group(ASH)) }
    val ZIRCONIUM_PICKAXE              by ITEMS.register("ceramic_zirconium_pickaxe") { PickaxeItem(PhoenixTiers.ZIRCONIUM_TIER, 0, -1f, Item.Properties().group(ASH)) }
    val ZIRCONIUM_SWORD                by ITEMS.register("ceramic_zirconium_sword")   { SwordItem  (PhoenixTiers.ZIRCONIUM_TIER, 2, -0.5f, Item.Properties().group(ASH)) }
    val ZIRCONIUM_KNIFE                by ITEMS.register("ceramic_zirconium_knife")   { KnifeItem  (PhoenixTiers.ZIRCONIUM_TIER, 3f, -10f, PhoenixConfiguration.COMMON_CONFIG.gameMode.get().maxKnifeUsages, ASH) }

    val STEEL_AXE                      by ITEMS.register("steel_axe")     { AxeItem    (PhoenixTiers.STEEL_TIER, 5.0f, -2f, Item.Properties().group(ASH)) }
    val STEEL_PICKAXE                  by ITEMS.register("steel_pickaxe") { PickaxeItem(PhoenixTiers.STEEL_TIER, 0,    -2f, Item.Properties().group(ASH)) }
    val STEEL_SWORD                    by ITEMS.register("steel_sword")   { SwordItem  (PhoenixTiers.STEEL_TIER, 2,    -2f, Item.Properties().group(ASH)) }

    val STEEL_ARMOR_HEAD               by ITEMS.register("steel_armor_head")      { SteelArmorItem(EquipmentSlotType.HEAD, Item.Properties().group(ASH)) }
    val STEEL_ARMOR_CHES               by ITEMS.register("steel_armor_chestplate"){ SteelArmorItem(EquipmentSlotType.CHEST,Item.Properties().group(ASH)) }
    val STEEL_ARMOR_LEGG               by ITEMS.register("steel_armor_leggings")  { SteelArmorItem(EquipmentSlotType.LEGS, Item.Properties().group(ASH)) }
    val STEEL_ARMOR_BUTS               by ITEMS.register("steel_armor_boots")     { SteelArmorItem(EquipmentSlotType.FEET, Item.Properties().group(ASH)) }

    fun register() = ITEMS.register(MOD_BUS)

    private fun basicItem()                             : () -> Item  = { Item(Item.Properties().group(ASH))                               }
    private fun basicItem(prop : Item.Properties)       : () -> Item  = { Item(prop)                                                       }
    private fun basicFood(food: Food)                   : () -> Item  = { Item(Item.Properties().group(ASH).food(food))                    }
    private fun basicFood(food: Food, group : ItemGroup): () -> Item  = { Item(Item.Properties().group(group).food(food))                  }
    private fun form(contains: () -> Item)                    : () -> Item  = { Item(Item.Properties().group(ASH).containerItem(contains())) }
}

class SteelArmorItem(slot: EquipmentSlotType, builder: Properties) : ArmorItem(PhoenixArmorMaterials.STEEL, slot, builder)
{
    override fun getArmorTexture(
        itemstack: ItemStack?,
        entity: Entity?,
        slot: EquipmentSlotType?,
        layer: String?
    ): String
    {
        return if (slot == EquipmentSlotType.LEGS)
        {
            Phoenix.MOD_ID + ":textures/models/armor/steel_layer_2.png"
        } else
        {
            Phoenix.MOD_ID + ":textures/models/armor/steel_layer_1.png"
        }
    }
}