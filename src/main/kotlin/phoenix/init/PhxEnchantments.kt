package phoenix.init

import net.minecraftforge.registries.ForgeRegistries
import phoenix.MOD_ID
import phoenix.Phoenix
import phoenix.enchantments.TeleportationEnchant
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxEnchantments
{
    var ENCHANTMENTS = KDeferredRegister(ForgeRegistries.ENCHANTMENTS, MOD_ID)

    val TELEPORTATION by ENCHANTMENTS.register("teleportation") { TeleportationEnchant }

    fun register() = ENCHANTMENTS.register(MOD_BUS)
}