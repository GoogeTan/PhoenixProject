package phoenix.utils;

import com.google.common.collect.Lists;
import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.inventory.container.*;
import phoenix.integration.minecraft.BookCategories;

import java.util.List;

public class Other
{
    public static List<RecipeBookCategories> getRecipeBookCategories(RecipeBookContainer<?> container)
    {
        if (!(container instanceof WorkbenchContainer) && !(container instanceof PlayerContainer))
        {
            if (container instanceof FurnaceContainer)
            {
                return Lists.newArrayList(
                        RecipeBookCategories.FURNACE_SEARCH,
                        RecipeBookCategories.FURNACE_FOOD,
                        RecipeBookCategories.FURNACE_BLOCKS,
                        RecipeBookCategories.FURNACE_MISC);
            }
            else if (container instanceof BlastFurnaceContainer)
            {
                return Lists.newArrayList(
                        RecipeBookCategories.BLAST_FURNACE_SEARCH,
                        RecipeBookCategories.BLAST_FURNACE_BLOCKS,
                        RecipeBookCategories.BLAST_FURNACE_MISC);
            }
            else
            {
                return container instanceof SmokerContainer ?
                        Lists.newArrayList(RecipeBookCategories.SMOKER_SEARCH, RecipeBookCategories.SMOKER_FOOD) :
                        Lists.newArrayList();
            }
        }
        else
        {
            return Lists.newArrayList(
                    RecipeBookCategories.SEARCH,
                    RecipeBookCategories.EQUIPMENT,
                    RecipeBookCategories.BUILDING_BLOCKS,
                    RecipeBookCategories.MISC,
                    RecipeBookCategories.REDSTONE,
                    BookCategories.INSTANCE.getMECHANISMS());
        }
    }

}
