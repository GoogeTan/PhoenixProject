package phoenix

import net.minecraft.block.Block
import net.minecraft.item.*
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

    private object ItemStackComparator : Comparator<ItemStack>
    {
        override fun compare(i1: ItemStack, i2: ItemStack): Int
        {
            val f = getWeight(i1)
            val s = getWeight(i2)
            return when
            {
                f > s -> 1
                f < s -> -1
                else -> i1.displayName.formattedText.compareTo(i2.displayName.formattedText)
            }
        }

        private fun getWeight(i1: ItemStack): Int
        {
            return when (i1.item)
            {
                is BlockItem -> 1
                is ToolItem  -> 2
                is ArmorItem -> 3
                else         -> 4
            }
        }
    }
}