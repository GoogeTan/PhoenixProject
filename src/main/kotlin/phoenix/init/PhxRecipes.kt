package phoenix.init

import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import phoenix.recipes.DryerRecipe
import phoenix.recipes.OvenRecipe

object PhxRecipes
{
    lateinit var OVEN : IRecipeType<OvenRecipe>
    lateinit var DRYER : IRecipeType<DryerRecipe>

    fun register()
    {
        OVEN = register("oven_recipe")
        DRYER = register("dryer_recipe")
    }

    fun <T : IRecipe<*>> register(key: String): IRecipeType<T>
    {
        return Registry.register(Registry.RECIPE_TYPE, ResourceLocation(key), object : IRecipeType<T> { override fun toString() = key })
    }
}