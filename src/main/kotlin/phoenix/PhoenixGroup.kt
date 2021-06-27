package phoenix

import net.minecraft.item.*
import net.minecraft.util.IItemProvider
import net.minecraft.util.NonNullList
import phoenix.items.ash.KnifeItem

class PhoenixGroup(name: String, private val item: () -> IItemProvider) : ItemGroup(name)
{
    constructor(name: String, item: IItemProvider) : this(name, {item})

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

        private fun getWeight(i1: ItemStack): Double
        {
            return when (i1.getItem())
            {
                is BlockItem   -> 1.0
                is SwordItem   -> 1.1
                is AxeItem     -> 1.2
                is PickaxeItem -> 1.3
                is ShovelItem  -> 1.4
                is KnifeItem   -> 1.5
                is ToolItem    -> 1.6
                is ArmorItem   -> 2.0
                else           -> 3.0
            }
        }
    }
}