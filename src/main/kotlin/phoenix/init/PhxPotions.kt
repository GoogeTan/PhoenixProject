package phoenix.init

import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.potion.Potion
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object PhxPotions
{
    val POTIONS = KDeferredRegister(ForgeRegistries.POTION_TYPES, Phoenix.MOD_ID)

    val LEVITATION       by POTIONS.register("levitation")      { Potion(EffectInstance(Effects.LEVITATION, 1800)) }
    val LONG_LEVITATION  by POTIONS.register("long_levitation") { Potion(EffectInstance(Effects.LEVITATION, 3600)) }

    fun register() = POTIONS.register(MOD_BUS)
}