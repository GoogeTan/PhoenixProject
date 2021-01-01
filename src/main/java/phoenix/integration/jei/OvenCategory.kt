package phoenix.integration.jei

import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import phoenix.Phoenix
import phoenix.init.PhoenixBlocks
import phoenix.recipes.OvenRecipe

class OvenCategory(private val helper: IGuiHelper) : IRecipeCategory<OvenRecipe>
{
    private val background: IDrawable = helper.drawableBuilder(ResourceLocation(Phoenix.MOD_ID, "textures/gui/oven.png"), 0, 0, 64, 32).setTextureSize(64, 32).build()
    override fun getUid        (): ResourceLocation      = ResourceLocation(Phoenix.MOD_ID, "oven")
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
        guiItemStacks.init(0, true, 6, 8)
        guiItemStacks.init(1, false, 40, 8)
        guiItemStacks.set(ingredients)
    }
}