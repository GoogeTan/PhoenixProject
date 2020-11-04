package phoenix.recipes.tank

import com.google.common.collect.ImmutableList
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.AbstractCookingRecipe
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import phoenix.init.PhoenixRecipeSerializers
import phoenix.init.PhoenixRecipes

class TankRecipe(id: ResourceLocation?, group: String?, ingr: Ingredient, result: ItemStack?, exp: Float, cookTime: Int)
    : AbstractCookingRecipe(PhoenixRecipes.OVEN, id, group, ingr, result, exp, cookTime)
{
    val inputs: MutableSet<Item> = HashSet()
    val recipes_from_inputs = HashMap<Item, TankRecipe>()

    init
    {
        for (stack in ingr.matchingStacks)
        {
            inputs.add(stack.item)
            recipes_from_inputs[stack.item] = this
        }
    }

    override fun getSerializer(): IRecipeSerializer<*>? = PhoenixRecipeSerializers.OVEN.get()
    fun getIngredient(): Ingredient = ingredient
    fun getResult(): ItemStack = result

    fun getInputs (): List<List<ItemStack>?>? = ImmutableList.of(ImmutableList.copyOf(getIngredient().matchingStacks))
    fun getOutputs(): List<List<ItemStack>?>? = ImmutableList.of(ImmutableList.of(getResult()))

}
