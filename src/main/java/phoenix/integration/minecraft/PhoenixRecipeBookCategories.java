package phoenix.integration.minecraft;

import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import phoenix.init.PhoenixBlocks;
import phoenix.utils.EnumUtil;

public class PhoenixRecipeBookCategories
{
    public static final RecipeBookCategories MECHANISMS = EnumUtil.addEnum(RecipeBookCategories.class,
            "MECHANISMS", new Class[]{ItemStack[].class}, (Object) new ItemStack[]{new ItemStack(Items.COMPARATOR)});
    public static final RecipeBookCategories OVEN = EnumUtil.addEnum(RecipeBookCategories.class,
            "OVEN", new Class[]{ItemStack[].class}, (Object) new ItemStack[]{new ItemStack(PhoenixBlocks.INSTANCE.getOVEN().get())});

}
