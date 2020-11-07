package phoenix.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import phoenix.init.PhoenixBlocks;
import phoenix.init.PhoenixRecipes;

import javax.annotation.Nullable;
import java.util.Collection;

@JeiPlugin
public class PhoenixJEIPlugin implements IModPlugin
{
    @Nullable
    private OvenCategory ovenCategory;
    @Nullable
    private TankCategory tankCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation("phoenix", "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration)
    {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        IModIdHelper modIdHelper = jeiHelpers.getModIdHelper();

        this.ovenCategory = new OvenCategory(guiHelper);
        registration.addRecipeCategories(ovenCategory);

        this.tankCategory = new TankCategory(guiHelper);
        registration.addRecipeCategories(tankCategory);
    }

    @Override
    public void registerRecipes(IRecipeRegistration register)
    {
        assert Minecraft.getInstance().world != null;
        RecipeManager manager = Minecraft.getInstance().world.getRecipeManager();
        // oven
        Collection<IRecipe<IInventory>> ovenRecipes = manager.getRecipes(PhoenixRecipes.OVEN).values();
        register.addRecipes(ovenRecipes, ovenCategory.getUid());
        // tank
        Collection<IRecipe<IInventory>> tankRecipes = manager.getRecipes(PhoenixRecipes.TANK).values();
        register.addRecipes(tankRecipes, tankCategory.getUid());
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
    {
        registration.addRecipeCatalyst(new ItemStack(PhoenixBlocks.OVEN.get()), ovenCategory.getUid());
        registration.addRecipeCatalyst(new ItemStack(PhoenixBlocks.TANK.get()), tankCategory.getUid());
    }
}