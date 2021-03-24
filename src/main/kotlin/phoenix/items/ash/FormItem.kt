package phoenix.items.ash

import net.minecraft.item.Item
import net.minecraftforge.fml.RegistryObject
import phoenix.Phoenix

class FormItem(var container : Item) : Item(Properties().group(Phoenix.ASH).containerItem(container))
{
    constructor(item : RegistryObject<Item>) : this(item.get())
}