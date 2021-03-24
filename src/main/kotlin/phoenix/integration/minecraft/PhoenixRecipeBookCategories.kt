package phoenix.integration.minecraft

import net.minecraft.client.util.RecipeBookCategories
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import phoenix.init.PhoenixBlocks
import phoenix.utils.EnumUtil

object PhoenixRecipeBookCategories
{
    val MECHANISMS = EnumUtil.addEnum(RecipeBookCategories::class.java, "MECHANISMS", arrayOf<Class<*>>(Array<ItemStack>::class.java), arrayOf(ItemStack(Items.COMPARATOR)) as Any)
    val OVEN       = EnumUtil.addEnum(RecipeBookCategories::class.java, "OVEN", arrayOf<Class<*>>(Array<ItemStack>::class.java), arrayOf(ItemStack(PhoenixBlocks.OVEN)) as Any)
}
