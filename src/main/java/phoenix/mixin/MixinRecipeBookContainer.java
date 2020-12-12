package phoenix.mixin;

import net.minecraft.client.util.RecipeBookCategories;
import net.minecraft.inventory.container.RecipeBookContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import phoenix.Phoenix;
import phoenix.utils.Other;

import java.util.List;

@Mixin(RecipeBookContainer.class)
public abstract class MixinRecipeBookContainer
{
    @Inject(method = "getRecipeBookCategories", at = @At("RETURN"), cancellable = true)
    public void getRecipeBookCategories(CallbackInfoReturnable<List<RecipeBookCategories>> cir)
    {
       Object o = this;
       if(o instanceof RecipeBookContainer)
       {
           cir.setReturnValue(Other.getRecipeBookCategories((RecipeBookContainer) o, cir));
           Phoenix.getLOGGER().error(cir.getReturnValue());
       }
    }
}
