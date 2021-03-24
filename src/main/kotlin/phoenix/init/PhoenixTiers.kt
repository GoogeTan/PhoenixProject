package phoenix.init

import net.minecraft.item.IItemTier
import net.minecraft.item.crafting.Ingredient


enum class PhoenixTiers(private val harvestLevel: Int, private val maxUses: Int, private val efficiency: Float, private val attackDamage: Float, private val enchantability: Int, private val repairMaterial: Ingredient) : IItemTier
{
    ZIRCONIUM_TIER(3, 1000, 6.0f, 2.0f, 14, Ingredient.fromItems(PhoenixItems.HIGH_QUALITY_CLAY_ITEM)),
    STEEL_TIER(2, 1200, 6f, 3.5f, 10, Ingredient.fromItems(PhoenixItems.STEEL_INGOT));

    override fun getEnchantability(): Int = enchantability
    override fun getHarvestLevel  (): Int = harvestLevel
    override fun getMaxUses       (): Int = maxUses
    override fun getEfficiency  (): Float = efficiency
    override fun getAttackDamage(): Float = attackDamage
    override fun getRepairMaterial(): Ingredient = repairMaterial
}