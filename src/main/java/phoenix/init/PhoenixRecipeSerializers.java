package phoenix.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.recipes.OvenRecipeSerializer;
import phoenix.recipes.tank.TankRecipeSerializer;

public class PhoenixRecipeSerializers
{
    private static final DeferredRegister<IRecipeSerializer<?>> RS = new DeferredRegister<>(ForgeRegistries.RECIPE_SERIALIZERS, Phoenix.MOD_ID);

    public static final RegistryObject<OvenRecipeSerializer> OVEN = RS.register("oven_recipe", OvenRecipeSerializer::new);
    public static final RegistryObject<TankRecipeSerializer> TANK = RS.register("tank_recipe", TankRecipeSerializer::new);

    public static void register()
    {
        RS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
