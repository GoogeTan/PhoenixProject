package phoenix.init

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.ContainerType
import net.minecraftforge.registries.ForgeRegistries
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.enity.CaudaEntity
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxContainers
{
    private val CONTAINERS = KDeferredRegister(ForgeRegistries.CONTAINERS, MOD_ID)

    val CAUDA by CONTAINERS.register("cauda") { ContainerType{ id : Int, player : PlayerInventory -> (player.player.ridingEntity as CaudaEntity).CaudaContainer(id, player)  } }

    fun register() = CONTAINERS.register(MOD_BUS)
}