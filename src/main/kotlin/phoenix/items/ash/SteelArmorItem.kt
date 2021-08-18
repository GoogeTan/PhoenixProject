package phoenix.items.ash

import net.minecraft.entity.Entity
import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.ArmorItem
import net.minecraft.item.ItemStack
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.init.PhxArmorMaterials

class SteelArmorItem(slot: EquipmentSlotType, builder: Properties) : ArmorItem(PhxArmorMaterials.STEEL, slot, builder)
{
    override fun getArmorTexture(itemstack: ItemStack?, entity: Entity?, slot: EquipmentSlotType?, layer: String?) = "${MOD_ID}:textures/models/armor/steel_layer_${if (slot == EquipmentSlotType.LEGS) 2 else 1}.png"
}
