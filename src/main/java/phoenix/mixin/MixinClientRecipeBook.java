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
import phoenix.Phoenix;
import phoenix.integration.minecraft.BookCategories;
import phoenix.recipes.OvenRecipe;

import java.util.List;
import java.util.Map;

@Mixin(ClientRecipeBook.class)
public class MixinClientRecipeBook
{
    @Final
    @Shadow()
    private final Map<RecipeBookCategories, List<RecipeList>> recipesByCategory = Maps.newHashMap();

    @Final
    @Shadow()
    private final List<RecipeList> allRecipes = Lists.newArrayList();

    @Shadow()
    private void func_216767_a(RecipeBookCategories p_216767_1_, RecipeList p_216767_2_) {
        this.recipesByCategory.computeIfAbsent(p_216767_1_, (p_216768_0_) -> Lists.newArrayList()).add(p_216767_2_);
    }

    @Inject(method = "newRecipeList", at = @At("RETURN"), cancellable = true)
    private void newRecipeList(RecipeBookCategories p_202889_1_, CallbackInfoReturnable<RecipeList> cir) {
        RecipeList recipelist = new RecipeList();
        this.allRecipes.add(recipelist);
        this.recipesByCategory.computeIfAbsent(p_202889_1_, (recipeBookCategories) -> Lists.newArrayList()).add(recipelist);
        if(p_202889_1_ == BookCategories.INSTANCE.getMECHANISMS())
        {
            this.func_216767_a(BookCategories.INSTANCE.getMECHANISMS(), recipelist);
        }
        cir.setReturnValue(recipelist);
    }

    @Inject(method = "getCategory", at = @At("RETURN"), cancellable = true)
    private static void getCategory(IRecipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir)
    {
        if(recipe instanceof OvenRecipe)
            cir.setReturnValue(BookCategories.INSTANCE.getMECHANISMS());
    }
}
