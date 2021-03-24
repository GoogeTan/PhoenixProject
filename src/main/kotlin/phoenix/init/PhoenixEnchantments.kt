package phoenix.init

import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.enchantments.TeleportationEnchant
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhoenixEnchantments
{
    var ENCHANTMENTS = KDeferredRegister(ForgeRegistries.ENCHANTMENTS, Phoenix.MOD_ID)

    val TELEPORTATION by ENCHANTMENTS.register("teleportation", ::TeleportationEnchant)

    fun register() = ENCHANTMENTS.register(MOD_BUS)
}