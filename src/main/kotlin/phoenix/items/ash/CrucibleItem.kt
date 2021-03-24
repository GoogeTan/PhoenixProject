package phoenix.items.ash

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import phoenix.Phoenix
import phoenix.init.PhoenixItems

class CrucibleItem : Item(Properties().group(Phoenix.ASH).maxStackSize(1).containerItem(PhoenixItems.CRUCIBLE.get()))
{
    override fun hasContainerItem(): Boolean
    {
        return true
    }

    override fun getContainerItem(stack: ItemStack): ItemStack
    {
        return ItemStack(PhoenixItems.CRUCIBLE.get())
    }
}