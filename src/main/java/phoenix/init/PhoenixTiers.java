package phoenix.init;

import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;

public enum PhoenixTiers implements IItemTier
{
    ZIRCONIUM_TIER(3, 1500, 6.0F, 4.0F, 10, Ingredient.fromItems(PhoenixItems.HIGH_QUALITY_CLAY_ITEM.get()));

    private final int harvestLevel;
    private final int maxUses;
    private final float efficiency;
    private final float attackDamage;
    private final int enchantability;
    private final Ingredient repairMaterial;

    PhoenixTiers(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Ingredient repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = repairMaterialIn;
    }

    @Override public int getMaxUses()               {  return this.maxUses;        }
    @Override public float getEfficiency()          {  return this.efficiency;     }
    @Override public float getAttackDamage()        {  return this.attackDamage;   }
    @Override public int getHarvestLevel()          {  return this.harvestLevel;   }
    @Override public int getEnchantability()        {  return this.enchantability; }
    @Override public Ingredient getRepairMaterial() { return this.repairMaterial;  }
}
