package phoenix.items.ash

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.RegistryObject
import phoenix.Phoenix
import phoenix.init.PhoenixItems

class FormItem(var container : ItemStack) : Item(Properties().group(Phoenix.PHOENIX))
{
    constructor() : this(ItemStack(PhoenixItems.CRUCIBLE.get()))
    constructor(item : Item) : this(ItemStack(item))
    constructor(item : RegistryObject<Item>) : this(item.get())

    override fun hasContainerItem(): Boolean = true

    override fun getContainerItem(stack: ItemStack?): ItemStack = container
}