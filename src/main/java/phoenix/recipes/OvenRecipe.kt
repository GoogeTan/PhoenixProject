package phoenix.recipes

import com.google.common.collect.ImmutableList
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.AbstractCookingRecipe
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import phoenix.init.PhoenixRecipeSerializers
import phoenix.init.PhoenixRecipeSerializers.OVEN
import phoenix.init.PhoenixRecipes

class OvenRecipe(
    idIn: ResourceLocation,
    groupIn: String,
    ingredientIn: Ingredient,
    resultIn: ItemStack,
    experienceIn: Float,
    cookTimeIn: Int
) :
    AbstractCookingRecipe(PhoenixRecipes.OVEN, idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn)
{
    override fun getSerializer(): IRecipeSerializer<OvenRecipe> = OVEN.get()

    val ingredient: Ingredient
        get() = ingredient
    val result: ItemStack
        get() = result.copy()

    override fun getExperience(): Float = experience
    override fun getCookTime(): Int = cookTime

    override fun getGroup(): String = group

    val inputs: List<List<ItemStack>>
        get()
        = ImmutableList.of<List<ItemStack>>(ImmutableList.copyOf(ingredient.matchingStacks))
    val outputs: List<List<ItemStack>>
        get()
        = ImmutableList.of<List<ItemStack>>(ImmutableList.of(result))

    override fun toString(): String
    {
        return "OvenRecipe{ingredient=$ingredient, result=$result, cookTime=$cookTime}"
    }

    companion object
    {
        var recipesFromInputs = HashMap<Item, OvenRecipe>()
    }

    init
    {
        for (stack in ingredientIn.matchingStacks)
        {
            recipesFromInputs[stack.item] = this
        }
    }
}