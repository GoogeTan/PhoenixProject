package phoenix.integration.minecraft

import net.minecraft.client.util.RecipeBookCategories
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import phoenix.init.PhxBlocks
import phoenix.utils.EnumUtil

object PhoenixRecipeBookCategories
{
    val MECHANISMS = EnumUtil.addEnum(RecipeBookCategories::class.java, "MECHANISMS", arrayOf<Class<*>>(Array<ItemStack>::class.java), arrayOf(ItemStack(Items.COMPARATOR)) as Any)
    val OVEN       = EnumUtil.addEnum(RecipeBookCategories::class.java, "oven", arrayOf<Class<*>>(Array<ItemStack>::class.java), arrayOf(ItemStack(PhxBlocks.oven)) as Any)
}
