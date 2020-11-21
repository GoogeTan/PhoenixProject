package phoenix.integration.jei

import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import phoenix.client.gui.OvenScreen
import phoenix.init.PhoenixBlocks
import phoenix.recipes.OvenRecipe

class OvenCategory(val helper: IGuiHelper) : IRecipeCategory<OvenRecipe>
{
    private val background: IDrawable = helper.createDrawable(OvenScreen.OVEN_TEXTURE, 0, 0, 100, 34)
    override fun getUid        (): ResourceLocation      = ResourceLocation("phoenix", "oven")
    override fun getRecipeClass(): Class<out OvenRecipe> = OvenRecipe::class.java
    override fun getTitle      (): String                = "Oven"
    override fun getBackground (): IDrawable             = background

    override fun getIcon(): IDrawable =  helper.createDrawableIngredient(ItemStack(PhoenixBlocks.OVEN.get()))

    override fun setIngredients(recipe: OvenRecipe, ingredients: IIngredients)
    {
        ingredients.setInputLists(VanillaTypes.ITEM, recipe.inputs)
        ingredients.setOutputLists(VanillaTypes.ITEM, recipe.outputs)
    }

    override fun setRecipe(recipeLayout: IRecipeLayout, recipe: OvenRecipe, ingredients: IIngredients)
    {
        val guiItemStacks = recipeLayout.itemStacks
        guiItemStacks.init(0, true, 8, 8)
        guiItemStacks.init(1, false, 42, 8)
        guiItemStacks.set(ingredients)
    }
}