package phoenix.integration.jei;

import mezz.jei.api.gui.ingredient.IGuiIngredient;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;

public class OvenRecipeDisplayData
{
    @Nullable
    private Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients = null;
    @Nullable
    private ItemStack lastLeftStack;
    @Nullable
    private ItemStack lastRightStack;
    private int lastCost;

    public OvenRecipeDisplayData()
    {
    }

    @Nullable
    public Map<Integer, ? extends IGuiIngredient<ItemStack>> getCurrentIngredients() {
        return this.currentIngredients;
    }

    public void setCurrentIngredients(Map<Integer, ? extends IGuiIngredient<ItemStack>> currentIngredients) {
        this.currentIngredients = currentIngredients;
    }

    @Nullable
    public ItemStack getLastLeftStack() {
        return this.lastLeftStack;
    }

    @Nullable
    public ItemStack getLastRightStack() {
        return this.lastRightStack;
    }

    public int getLastCost() {
        return this.lastCost;
    }

    public void setLast(ItemStack leftStack, ItemStack rightStack, int lastCost) {
        this.lastLeftStack = leftStack;
        this.lastRightStack = rightStack;
        this.lastCost = lastCost;
    }
}
