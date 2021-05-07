package phoenix.init

import net.minecraft.inventory.EquipmentSlotType
import net.minecraft.item.IArmorMaterial
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.SoundEvent
import net.minecraft.util.SoundEvents

enum class PhxArmorMaterials(
    private val durability: Float,
    private val damageReductionAmount: Array<Int>,
    private val enchantability: Int,
    private val sound: SoundEvent,
    private val repair: () -> Ingredient,
    private val nameIn: String,
    private val toughness: Float) : IArmorMaterial
{
    STEEL(20f, arrayOf(2, 5, 6, 3), 12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, { Ingredient.fromItems(PhxItems.STEEL_INGOT)}, "steel", 0f),
    CERAMIC(30f, arrayOf(2, 5, 6, 4), 25, SoundEvents.ITEM_ARMOR_EQUIP_IRON, { Ingredient.fromItems(PhxItems.STEEL_INGOT)}, "ceramic", 1f);

    private val maxDamage = intArrayOf(13, 15, 16, 11)

    override fun getDurability(slotIn: EquipmentSlotType)            = (maxDamage[slotIn.index] * durability).toInt()
    override fun getDamageReductionAmount(slotIn: EquipmentSlotType) = damageReductionAmount[slotIn.index]
    override fun getEnchantability()                                 = enchantability
    override fun getSoundEvent()                                     = sound
    override fun getRepairMaterial()                                 = repair()
    override fun getName()                                           = nameIn
    override fun getToughness()                                      = toughness
}