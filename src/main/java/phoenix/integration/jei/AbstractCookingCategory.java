package phoenix.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.util.text.LanguageMap;

public abstract class AbstractCookingCategory<T extends AbstractCookingRecipe> extends FurnaceVariantCategory<T> {
    private final IDrawable background;
    private final IDrawable icon;
    private final String localizedName;
    protected final IDrawableAnimated arrow;

    public AbstractCookingCategory(IGuiHelper guiHelper, Block icon, String translationKey, int regularCookTime) {
        super(guiHelper);
        this.background = guiHelper.createDrawable(RECIPE_GUI_VANILLA, 0, 114, 82, 54);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(icon));
        this.localizedName = LanguageMap.getInstance().translateKey(translationKey);
        this.arrow = guiHelper.drawableBuilder(RECIPE_GUI_VANILLA, 82, 128, 24, 17).buildAnimated(regularCookTime, IDrawableAnimated.StartDirection.LEFT, false);
    }

    public IDrawable getBackground() {
        return this.background;
    }

    public IDrawable getIcon() {
        return this.icon;
    }

    public void setIngredients(T recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    public void draw(T recipe, double mouseX, double mouseY) {
        this.animatedFlame.draw(1, 20);
        this.arrow.draw(24, 18);
        float experience = recipe.getExperience();
        if (experience > 0.0F) {
            String experienceString = LanguageMap.getInstance().translateKey("gui.jei.category.smelting.experience");
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.fontRenderer;
            int stringWidth = fontRenderer.getStringWidth(experienceString);
            fontRenderer.drawString(experienceString, (float)(this.background.getWidth() - stringWidth), 0.0F, -8355712);
        }

    }

    public String getTitle() {
        return this.localizedName;
    }

    public void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 0, 0);
        guiItemStacks.init(2, false, 60, 18);
        guiItemStacks.set(ingredients);
    }
}

