package phoenix.mixin;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.util.ClientRecipeBook;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.item.crafting.IRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phoenix.init.PhoenixRecipes;
import phoenix.integration.minecraft.PhoenixRecipeBookCategories;
import phoenix.utils.LogManager;

import java.util.List;
import java.util.Map;
/*
 * It had been stolen from The-Midnight. And then changed.
 */
@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin
{
    @SuppressWarnings("FieldMayBeFinal")
    @Shadow
    @Final
    private Map<RecipeBookCategories, List<RecipeList>> recipesByCategory = Maps.newHashMap();

    @SuppressWarnings("FieldMayBeFinal")
    @Shadow
    @Final
    private List<RecipeList> allRecipes = Lists.newArrayList();

    @Inject(at = @At("HEAD"), method = "newRecipeList", cancellable = true)
    private void newRecipeList(RecipeBookCategories categories, CallbackInfoReturnable<RecipeList> callback)
    {
        RecipeList list = newRecipeList(categories, allRecipes, recipesByCategory);

        if (list != null) {
            this.allRecipes.add(list);
            callback.setReturnValue(list);
        }
    }

    @Inject(at = @At("HEAD"), method = "getCategory", cancellable = true)
    private static void getCategory(IRecipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> callback)
    {
        RecipeBookCategories category = getRecipeCategory(recipe);
        if (category != null) {
            callback.setReturnValue(category);
        }
    }

    private static RecipeBookCategories getRecipeCategory(IRecipe<?> recipe) {
        if (recipe.getType() == PhoenixRecipes.OVEN) {
            return PhoenixRecipeBookCategories.OVEN;
        }
        return null;
    }

    private static RecipeList newRecipeList(RecipeBookCategories category, List<RecipeList> allRecipes, Map<RecipeBookCategories, List<RecipeList>> recsByCategory) {
        if (category == PhoenixRecipeBookCategories.OVEN)
        {
            RecipeList list = new RecipeList();
            allRecipes.add(list);
            recsByCategory.computeIfAbsent(category, cgr -> Lists.newArrayList()).add(list);
            recsByCategory.computeIfAbsent(PhoenixRecipeBookCategories.OVEN, cgr -> Lists.newArrayList()).add(list);
            return list;
        }

        return null;
    }
}
