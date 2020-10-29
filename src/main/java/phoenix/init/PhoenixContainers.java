package phoenix.init;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;
import phoenix.client.gui.DiaryGui;
import phoenix.containers.DiaryContainer;
import phoenix.containers.OvenContainer;

public class PhoenixContainers
{
    private static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Phoenix.MOD_ID);

    public static final RegistryObject<ContainerType<DiaryContainer>> GUIDE   = CONTAINERS.register("drls_diary", () -> new ContainerType<>(DiaryContainer::fromNetwork));
    public static final RegistryObject<ContainerType<OvenContainer>>  OVEN    = CONTAINERS.register("oven",       () -> new ContainerType<>(OvenContainer::fromNetwork));

    public static void register()
    {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    @OnlyIn(Dist.CLIENT)
    public static void renderScreens()
    {
        ScreenManager.registerFactory(GUIDE.get(), DiaryGui::new);
    }
}
