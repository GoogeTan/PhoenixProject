package phoenix.enchantments

import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentType
import net.minecraft.enchantment.Enchantments
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ItemStack

object TeleportationEnchant : Enchantment(Rarity.VERY_RARE, EnchantmentType.ALL, arrayOf(EquipmentSlotType.MAINHAND, EquipmentSlotType.OFFHAND))
{
    override fun canApplyTogether(enchantment: Enchantment) = super.canApplyTogether(enchantment) && enchantment !== Enchantments.LOYALTY
    override fun canApplyAtEnchantingTable(stack: ItemStack) = false
}