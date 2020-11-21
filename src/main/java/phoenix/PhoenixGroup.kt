package phoenix

import net.minecraft.block.Blocks
import net.minecraft.item.*
import net.minecraft.util.NonNullList
import java.util.*

class PhoenixGroup(name: String) : ItemGroup(name)
{
    override fun createIcon() = ItemStack(Blocks.END_PORTAL_FRAME)
    override fun hasSearchBar() = true

    override fun fill(items: NonNullList<ItemStack>)
    {
        super.fill(items)
        items.sortWith(comp())
    }

    internal class comp : Comparator<ItemStack>
    {
        override fun compare(i1: ItemStack, i2: ItemStack): Int
        {
            var f = 0
            var s = 0
            if (i1.item is ArmorItem) f = 2 else if (i1.item is SwordItem) f = 3 else if (i1.item is AxeItem) f = 4 else if (i1.item is PickaxeItem) f = 5 else if (i1.item is ToolItem) f = 1 else if (i1.item is BlockItem) f = 6
            if (i2.item is ArmorItem) s = 2 else if (i2.item is SwordItem) s = 3 else if (i2.item is AxeItem) s = 4 else if (i2.item is PickaxeItem) s = 5 else if (i2.item is ToolItem) s = 6 else if (i2.item is BlockItem) s = 7
            return Integer.compare(f, s)
        }
    }
}