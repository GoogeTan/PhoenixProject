package phoenix.init

import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry
import phoenix.Phoenix
import thedarkcolour.kotlinforforge.forge.KDeferredRegister
import thedarkcolour.kotlinforforge.forge.MOD_BUS

abstract class AbstractRegisterer<T : IForgeRegistryEntry<T>>(registry: IForgeRegistry<T>, modid : String = Phoenix.MOD_ID)
{
    val registerer : KDeferredRegister<T> = KDeferredRegister(registry, modid)
    fun register() = registerer.register(MOD_BUS)
}