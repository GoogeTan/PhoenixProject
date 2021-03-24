package phoenix.init

import net.minecraft.client.gui.ScreenManager
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.client.gui.DiaryGui
import phoenix.containers.DiaryContainer

object PhoenixContainers
{
    private val CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Phoenix.MOD_ID)

    val GUIDE = CONTAINERS.register("diary") { DiaryContainer.fromNetwork() }

    fun register() = CONTAINERS.register(FMLJavaModLoadingContext.get().modEventBus)

    @OnlyIn(Dist.CLIENT)
    fun registerScreens()
    {
        ScreenManager.registerFactory(GUIDE.get(), ::DiaryGui)
    }
}