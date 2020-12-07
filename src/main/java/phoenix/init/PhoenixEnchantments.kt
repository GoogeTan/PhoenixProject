package phoenix.init

import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import phoenix.enchantments.TeleportationEnchant

object PhoenixEnchantments
{
    var ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Phoenix.MOD_ID)!!

    var TELEPORTATION = ENCHANTMENTS.register("teleportation", ::TeleportationEnchant)!!

    fun register()
    {
        ENCHANTMENTS.register(FMLJavaModLoadingContext.get().modEventBus)
    }
}