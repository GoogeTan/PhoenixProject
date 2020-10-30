package phoenix.integration.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import phoenix.init.PhoenixBlocks;
import phoenix.recipes.OvenRecipe;

import static phoenix.integration.jei.FurnaceVariantCategory.RECIPE_GUI_VANILLA;

public class OvenCategory implements IRecipeCategory<OvenRecipe>
{
    private final IDrawable background;
    IGuiHelper helper;
    public OvenCategory(IGuiHelper guiHelper)
    {
        helper = guiHelper;
        this.background = guiHelper.createDrawable(RECIPE_GUI_VANILLA, 0, 114, 82, 54);
    }

    @Override
    public ResourceLocation getUid()
    {
        return new ResourceLocation("phoenix", "oven");
    }

    @Override
    public Class getRecipeClass()
    {
        return OvenRecipe.class;
    }

    @Override
    public String getTitle()
    {
        return "Oven";
    }

    @Override
    public IDrawable getBackground()
    {
        return this.background;
    }

    @Override
    public IDrawable getIcon()
    {
        return helper.createDrawableIngredient(new ItemStack(PhoenixBlocks.OVEN.get()));
    }

    @Override
    public void setIngredients(OvenRecipe ovenRecipe, IIngredients iIngredients)
    {

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, OvenRecipe ovenRecipe, IIngredients iIngredients)
    {

    }
}