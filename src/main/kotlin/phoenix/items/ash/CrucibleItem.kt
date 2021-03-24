package phoenix.items.ash

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import phoenix.Phoenix
import phoenix.init.PhoenixItems

class CrucibleItem : Item(Properties().group(Phoenix.ASH).maxStackSize(1).containerItem(PhoenixItems.CRUCIBLE))
{
    override fun hasContainerItem(): Boolean = true

    override fun getContainerItem(stack: ItemStack): ItemStack = ItemStack(PhoenixItems.CRUCIBLE)
}