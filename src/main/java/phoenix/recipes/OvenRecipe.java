package phoenix.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import phoenix.init.PhoenixRecipeSerializers;
import phoenix.init.PhoenixRecipes;

public class OvenRecipe extends AbstractCookingRecipe
{
    public OvenRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn)
    {
        super(PhoenixRecipes.OVEN, idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn);
    }

    @Override
    public IRecipeSerializer<?> getSerializer()
    {
        return PhoenixRecipeSerializers.OVEN.get();
    }

    public Ingredient getIngredient()
    {
        return ingredient;
    }

    public ItemStack getResult()
    {
        return result;
    }

    public float getExperience()
    {
        return experience;
    }

    public int getCookTime()
    {
        return cookTime;
    }

    public String getGroup()
    {
        return group;
    }
}
