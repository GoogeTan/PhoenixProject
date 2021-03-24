package phoenix.init

import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.potion.Potion
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import phoenix.Phoenix

object PhoenixPotions
{
    private val POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, Phoenix.MOD_ID)!!

    val LEVITATION       : RegistryObject<Potion> = POTIONS.register("levitation")      { Potion(EffectInstance(Effects.LEVITATION, 1800)) }!!
    val LONG_LEVITATION  : RegistryObject<Potion> = POTIONS.register("long_levitation") { Potion(EffectInstance(Effects.LEVITATION, 3600)) }!!

    fun register() = POTIONS.register(FMLJavaModLoadingContext.get().modEventBus)
}