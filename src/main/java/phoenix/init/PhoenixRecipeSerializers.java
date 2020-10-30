package phoenix.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.recipes.OvenRecipe;
import phoenix.recipes.OvenRecipeSerializer;

public class PhoenixRecipeSerializers
{
    private static final DeferredRegister<IRecipeSerializer<?>> RS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Phoenix.MOD_ID);

    public static final RegistryObject<OvenRecipeSerializer<OvenRecipe>> OVEN = RS.register("oven_recipe_serializer", () -> new OvenRecipeSerializer<>(OvenRecipe::new, 100));

    public static void register()
    {
        RS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
