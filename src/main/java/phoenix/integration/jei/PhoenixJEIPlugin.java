package phoenix.integration.jei;


import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import phoenix.Phoenix;
import phoenix.init.PhoenixBlocks;
import phoenix.init.PhoenixRecipes;
import phoenix.recipes.OvenRecipe;
import phoenix.recipes.PhoenixRecipeValidator;

import javax.annotation.Nullable;
import java.util.List;


@JeiPlugin
public class PhoenixJEIPlugin implements IModPlugin
{
    @Nullable
    private OvenCategory ovenCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("jei", "phoenix");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        IModIdHelper modIdHelper = jeiHelpers.getModIdHelper();
        this.ovenCategory = new OvenCategory(guiHelper);
        registration.addRecipeCategories(ovenCategory);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration)
    {
        List<OvenRecipe> recipes = PhoenixRecipeValidator.getValidRecipes(ovenCategory).getOvenRecipes();
        registration.addRecipes(recipes, ovenCategory.getUid());
        Phoenix.LOGGER.error(recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(PhoenixBlocks.OVEN.get()), ovenCategory.getUid());
    }
}