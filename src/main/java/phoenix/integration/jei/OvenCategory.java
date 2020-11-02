package phoenix.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import phoenix.client.gui.OvenScreen;
import phoenix.init.PhoenixBlocks;
import phoenix.recipes.OvenRecipe;

public class OvenCategory implements IRecipeCategory<OvenRecipe>
{
    private final IDrawable background;
    IGuiHelper helper;

    public OvenCategory(IGuiHelper guiHelper)
    {
        helper = guiHelper;
        this.background = guiHelper.createDrawable(OvenScreen.OVEN_TEXTURE, 0, 0, 100, 34);
    }

    @Override public ResourceLocation getUid () {  return new ResourceLocation("phoenix", "oven");  }
    @Override public Class     getRecipeClass() { return OvenRecipe.class;                                           }
    @Override public String    getTitle      () { return "Oven";                                                     }
    @Override public IDrawable getBackground () { return this.background;                                            }

    @Override
    public IDrawable getIcon()
    {
        return helper.createDrawableIngredient(new ItemStack(PhoenixBlocks.OVEN.get()));
    }

    @Override
    public void setIngredients(OvenRecipe recipe, IIngredients ingredients)
    {
        ingredients.setInputLists(VanillaTypes.ITEM, recipe.getInputs());
        ingredients.setOutputLists(VanillaTypes.ITEM, recipe.getOutputs());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, OvenRecipe recipe, IIngredients ingredients)
    {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 8, 8);
        guiItemStacks.init(1, false, 42, 8);
        guiItemStacks.set(ingredients);
    }
}