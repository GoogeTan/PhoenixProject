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
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhoenixContainers
{
    private val CONTAINERS = KDeferredRegister(ForgeRegistries.CONTAINERS, Phoenix.MOD_ID)

    val GUIDE by CONTAINERS.register("diary") { DiaryContainer.fromNetwork() }

    fun register() = CONTAINERS.register(MOD_BUS)

    @OnlyIn(Dist.CLIENT)
    fun registerScreens()
    {
        ScreenManager.registerFactory(GUIDE, ::DiaryGui)
    }
}