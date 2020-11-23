package phoenix

import net.minecraft.block.Blocks
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.util.NonNullList
import java.util.*

class PhoenixGroup(name: String) : ItemGroup(name)
{
    override fun createIcon() = ItemStack(Blocks.END_PORTAL_FRAME)
    override fun hasSearchBar() = true

    override fun fill(items: NonNullList<ItemStack>)
    {
        super.fill(items)
        items.sortWith(ItemStackComparator())
    }

    internal class ItemStackComparator : Comparator<ItemStack>
    {
        override fun compare(i1: ItemStack, i2: ItemStack): Int
        {
            val f = getWeight(i1)
            val s = getWeight(i2)
            return when
            {
                f > s -> 1
                f < s -> -1
                else  ->
                {
                    val a = i1.displayName
                    val b = i2.displayName
                    a.formattedText.compareTo(b.formattedText)
                }
            }
        }

        fun getWeight(i1: ItemStack): Int
        {
            return when (i1.item)
            {
                is BlockItem -> 1
                is ToolItem  -> 3
                else         -> 2
            }
        }
    }
}