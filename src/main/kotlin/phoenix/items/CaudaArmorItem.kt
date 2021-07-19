package phoenix.items

import net.minecraft.item.Item
import net.minecraft.item.Rarity
import phoenix.Phoenix
import phoenix.init.ICaudaArmorMaterial

class CaudaArmorItem(val material: ICaudaArmorMaterial) : Item(Properties().rarity(Rarity.RARE).group(Phoenix.REDO).maxStackSize(1))
