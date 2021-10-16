package me.zahara.phoenix.init.registery

import me.zahara.phoenix.modId
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import net.minecraftforge.registries.*
import kotlin.reflect.KClass

abstract class AutoRegisterer<T: IForgeRegistryEntry<T>>(val clazz : Class<T>, private val modid : String = modId)
{
    private val data : MutableList<Pair<ResourceLocation, () -> T>> = mutableListOf()
    lateinit var entries : List<RegisterEntryProperty<T, out T>>
        private set

    protected open fun<Type : T> register(name : String, value : () -> Type) : RegisterEntryProperty<T, Type> = register(ResourceLocation(modid, name), value)

    protected open fun<Type : T> register(name : ResourceLocation, value : () -> Type) : RegisterEntryProperty<T, Type>
    {
        data.add(Pair(name, value))
        return RegisterEntryProperty {
            RegistryManager.ACTIVE.getRegistry(clazz).getValue(name) as? Type? ?: throw Exception("Unregistered value $name exception")
        }
    }

    fun register() = FMLJavaModLoadingContext.get().modEventBus.addGenericListener(clazz) { event: RegistryEvent.Register<T> ->
        val entries = mutableListOf<RegisterEntryProperty<T, out T>>()
        for ((location, getter) in data)
        {
            val value = getter()
            value.registryName = location
            event.registry.register(value)
            entries.add(RegisterEntryProperty {
                event.registry.getValue(location) ?: throw Exception("Unregistered value $location exception")
            })
        }
        this.entries = entries
    }

    inline fun forEach(func : (value : T) -> Unit)
    {
        for (i in entries)
        {
            val p by i
            func(p)
        }
    }
}