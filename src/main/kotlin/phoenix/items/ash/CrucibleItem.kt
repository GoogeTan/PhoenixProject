package phoenix.items.ash

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import phoenix.Phoenix
import phoenix.init.PhxItems

class CrucibleItem : Item(Properties().group(Phoenix.ASH).maxStackSize(1).containerItem(PhxItems.CRUCIBLE))
{
    override fun hasContainerItem(): Boolean = true

    override fun getContainerItem(stack: ItemStack): ItemStack = ItemStack(PhxItems.CRUCIBLE)
}