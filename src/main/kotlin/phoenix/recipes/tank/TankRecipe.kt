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

class TankRecipe(id: ResourceLocation, group: String, inputIn: ItemStack, result: ItemStack, exp: Float, cookTime: Int)
    : AbstractCookingRecipe(PhoenixRecipes.OVEN, id, group, Ingredient.fromStacks(inputIn), result, exp, cookTime)
{
    private var input : ItemStack = inputIn
    private val inputs = HashSet<Item>()
    private val recipesFromInputs = HashMap<Item, TankRecipe>()

    init
    {
        inputs.add(inputIn.item)
        recipesFromInputs[inputIn.item] = this

    }

    override fun getSerializer(): IRecipeSerializer<*>? = PhoenixRecipeSerializers.TANK.get()
    fun getIngredient(): Ingredient = ingredient
    fun getResult(): ItemStack = result
    fun getInput(): ItemStack = input
    fun getInputs (): List<List<ItemStack>> = ImmutableList.of(ImmutableList.copyOf(getIngredient().matchingStacks))
    fun getOutputs(): List<List<ItemStack>> = ImmutableList.of(ImmutableList.of(getResult()))
}
