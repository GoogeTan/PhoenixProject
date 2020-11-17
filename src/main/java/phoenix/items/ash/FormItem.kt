package phoenix.items.ash

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.RegistryObject
import phoenix.Phoenix
import phoenix.init.PhoenixItems

class FormItem(var container : Item) : Item(Properties().group(Phoenix.PHOENIX).containerItem(container))
{
    constructor(item : RegistryObject<Item>) : this(item.get())
}