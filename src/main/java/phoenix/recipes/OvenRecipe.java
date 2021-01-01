package phoenix.recipes;


import com.google.common.collect.ImmutableList;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import phoenix.init.PhoenixRecipeSerializers;
import phoenix.init.PhoenixRecipes;

import java.util.HashMap;
import java.util.List;

@MethodsReturnNonnullByDefault
public class OvenRecipe extends AbstractCookingRecipe
{
    public static HashMap<Item, OvenRecipe> recipes_from_inputs = new HashMap<>();
    public OvenRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn)
    {
        super(PhoenixRecipes.OVEN, idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn);
        for (ItemStack stack : ingredientIn.getMatchingStacks())
        {
            recipes_from_inputs.put(stack.getItem(), this);
        }
    }

    public static HashMap<Item, OvenRecipe> getRecipesFromInputs()
    {
        return recipes_from_inputs;
    }

    @Override
    public IRecipeSerializer<OvenRecipe> getSerializer()
    {
        return PhoenixRecipeSerializers.getOVEN().get();
    }

    public Ingredient getIngredient()
    {
        return ingredient;
    }

    public ItemStack getResult()
    {
        return result.copy();
    }

    public float getExperience()
    {
        return experience;
    }

    public int getCookTime()
    {
        return cookTime;
    }

    public String getGroup() { return group;    }

    public List<List<ItemStack>> getInputs() { return ImmutableList.of(ImmutableList.copyOf(getIngredient().getMatchingStacks())); }

    public List<List<ItemStack>> getOutputs() {
        return ImmutableList.of(ImmutableList.of(getResult()));
    }
    
    public String toString()
    {
        return "OvenRecipe{" + "ingredient=" + ingredient + ", result=" + result + ", cookTime=" + cookTime + '}';
    }
}