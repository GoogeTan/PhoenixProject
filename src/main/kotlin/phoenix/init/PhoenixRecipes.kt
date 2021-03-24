package phoenix.init

import net.minecraft.item.crafting.IRecipe
import net.minecraft.item.crafting.IRecipeType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import phoenix.recipes.OvenRecipe

object PhoenixRecipes
{
    lateinit var OVEN : IRecipeType<OvenRecipe>

    fun register()
    {
        OVEN = register("oven_recipe")
    }

    fun <T : IRecipe<*>> register(key: String): IRecipeType<T>
    {
        return Registry.register(Registry.RECIPE_TYPE, ResourceLocation(key), object : IRecipeType<T> { override fun toString() = key })
    }
}