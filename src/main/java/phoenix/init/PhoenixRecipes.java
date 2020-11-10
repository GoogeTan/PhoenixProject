package phoenix.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import phoenix.recipes.OvenRecipe;

public class PhoenixRecipes
{
    public static IRecipeType<OvenRecipe> OVEN;

    public static void register()
    {
        OVEN = register("oven_recipe");
    }

    static <T extends IRecipe<?>> IRecipeType<T> register(final String key)
    {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(key), new IRecipeType<T>()
        {
            public String toString()
            {
                return key;
            }
        });
    }
}