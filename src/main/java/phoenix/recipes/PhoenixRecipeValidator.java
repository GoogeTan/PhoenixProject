package phoenix.recipes;

import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phoenix.Phoenix;
import phoenix.init.PhoenixRecipes;

import java.util.*;

public class PhoenixRecipeValidator
{
    private static final Logger LOGGER = LogManager.getLogger();

    private PhoenixRecipeValidator()
    {
    }

    public static PhoenixRecipeValidator.Results getValidRecipes(IRecipeCategory<OvenRecipe> ovenCategory)
    {
        CategoryRecipeValidator<OvenRecipe> ovenRecipesValidator = new CategoryRecipeValidator(ovenCategory, 1);
        Results results = new Results();
        ClientWorld world = Minecraft.getInstance().world;
        RecipeManager recipeManager = world.getRecipeManager();

        for (OvenRecipe recipe : getRecipes(recipeManager, PhoenixRecipes.OVEN))
        {
            if (ovenRecipesValidator.isRecipeValid(recipe))
            {
                results.ovenRecipes.add(recipe);
            }
        }

        return results;
    }

    private static <C extends IInventory, T extends IRecipe<C>> Collection<T> getRecipes(RecipeManager recipeManager, IRecipeType<T> recipeType)
    {
        Map<ResourceLocation, IRecipe<C>> recipesMap = recipeManager.getRecipes(recipeType);
        return (Collection<T>) recipesMap.values();
    }

    private static final class CategoryRecipeValidator<T extends IRecipe<?>>
    {
        private final IRecipeCategory<T> recipeCategory;
        private final int maxInputs;

        public CategoryRecipeValidator(IRecipeCategory<T> recipeCategoryIn, int maxInputs)
        {
            this.recipeCategory = recipeCategoryIn;
            this.maxInputs = maxInputs;
        }

        public boolean isRecipeValid(T recipe)
        {
            if (recipe.isDynamic())
            {
                return false;
            } else
            {
                ItemStack recipeOutput = recipe.getRecipeOutput();
                if (recipeOutput != null && !recipeOutput.isEmpty())
                {
                    List<Ingredient> ingredients = recipe.getIngredients();
                    if (ingredients == null)
                    {
                        LOGGER.error("Recipe has no input Ingredients.");
                        return false;
                    } else
                    {
                        int inputCount = getInputCount(ingredients);
                        if (inputCount == -1)
                        {
                            return false;
                        } else
                        {
                            String recipeInfo;
                            if (inputCount > this.maxInputs)
                            {
                                LOGGER.error("Recipe has too many inputs.");
                                return false;
                            } else if (inputCount == 0)
                            {
                                LOGGER.error("Recipe has no inputs.");
                                return false;
                            } else
                            {
                                return true;
                            }
                        }
                    }
                } else
                {
                    LOGGER.error("Recipe has no output.");
                    return false;
                }
            }
        }


        protected static int getInputCount(List<Ingredient> ingredientList)
        {
            int inputCount = 0;

            for (Iterator var2 = ingredientList.iterator(); var2.hasNext(); ++inputCount)
            {
                Ingredient ingredient = (Ingredient) var2.next();
                ItemStack[] input = ingredient.getMatchingStacks();
                if (input == null)
                {
                    return -1;
                }
            }

            return inputCount;
        }
    }

    public static class Results
    {
        private final List<OvenRecipe> ovenRecipes = new ArrayList();

        public Results()
        {
        }

        public List<OvenRecipe> getOvenRecipes()
        {
            return this.ovenRecipes;
        }
    }
}
