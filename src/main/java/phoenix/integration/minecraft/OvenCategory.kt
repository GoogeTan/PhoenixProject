package phoenix.integration.minecraft

import net.minecraft.block.Blocks
import net.minecraft.client.util.RecipeBookCategories
import net.minecraft.item.ItemStack
import phoenix.init.PhoenixBlocks
import phoenix.utils.EnumHelper

object BookCategories
{
    var MECHANISMS : RecipeBookCategories? = EnumHelper.addRecipeBookCategory("mechanisms", arrayOf(ItemStack(PhoenixBlocks.UPDATER.orElse(Blocks.OBSERVER))))
}