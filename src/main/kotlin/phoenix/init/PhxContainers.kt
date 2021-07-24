package phoenix.init

import net.minecraft.client.gui.ScreenManager
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.client.gui.DiaryGui
import phoenix.containers.DiaryContainer
import phoenix.enity.CaudaEntity
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxContainers
{
    private val CONTAINERS = KDeferredRegister(ForgeRegistries.CONTAINERS, Phoenix.MOD_ID)

    val GUIDE by CONTAINERS.register("diary") { ContainerType(::DiaryContainer) }
    val CAUDA by CONTAINERS.register("cauda") { ContainerType{ id : Int, player : PlayerInventory -> (player.player.ridingEntity as CaudaEntity).CaudaContainer(id, player)  } }

    fun register() = CONTAINERS.register(MOD_BUS)

    @OnlyIn(Dist.CLIENT)
    fun registerScreens()
    {
        //ScreenManager.registerFactory(GUIDE, ::DiaryGui)
    }
}