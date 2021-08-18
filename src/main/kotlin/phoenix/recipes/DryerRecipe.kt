package phoenix.recipes

import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeSerializer
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.item.crafting.Ingredient
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import phoenix.init.PhxRecipeSerializers
import phoenix.init.PhxRecipes

class DryerRecipe(val idIn : ResourceLocation, val input : Ingredient, val result : ItemStack, val time : Int) : IRecipe<IInventory>
{
    override fun matches(inv: IInventory, worldIn: World): Boolean= input.test(inv.getStackInSlot(0))
    override fun getCraftingResult(inv: IInventory): ItemStack = result.copy()
    override fun canFit(width: Int, height: Int): Boolean = true
    override fun getRecipeOutput(): ItemStack = result
    override fun getId(): ResourceLocation = idIn
    override fun getSerializer(): IRecipeSerializer<*> = PhxRecipeSerializers.DRYER
    override fun getType(): IRecipeType<*> = PhxRecipes.DRYER


    companion object
    {
        var recipesFromInputs = HashMap<Item, DryerRecipe>()
        var recipesByResult = HashMap<Item, DryerRecipe>()
    }

    init
    {
        for (stack in input.matchingStacks)
        {
            recipesFromInputs[stack.getItem()] = this
        }
        recipesByResult[result.getItem()] = this
    }
}