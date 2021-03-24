package phoenix

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.util.IItemProvider
import net.minecraft.util.NonNullList
import net.minecraftforge.fml.RegistryObject

class PhoenixGroup(name: String, private val item: () -> IItemProvider) : ItemGroup(name)
{
    constructor(name: String, item: IItemProvider) : this(name, {item})

    constructor(name: String, item: RegistryObject<Block>) : this(name, item::get)

    override fun createIcon() = ItemStack(item.invoke())
    override fun hasSearchBar() = false

    override fun fill(items: NonNullList<ItemStack>)
    {
        super.fill(items)
        items.sortWith(ItemStackComparator)
    }

    internal object ItemStackComparator : Comparator<ItemStack>
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

        private fun getWeight(i1: ItemStack): Int
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