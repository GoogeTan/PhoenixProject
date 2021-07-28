package phoenix.init

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.enity.CaudaEntity
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxContainers
{
    private val CONTAINERS = KDeferredRegister(ForgeRegistries.CONTAINERS, Phoenix.MOD_ID)

    val CAUDA by CONTAINERS.register("cauda") { ContainerType{ id : Int, player : PlayerInventory -> (player.player.ridingEntity as CaudaEntity).CaudaContainer(id, player)  } }

    fun register() = CONTAINERS.register(MOD_BUS)
}