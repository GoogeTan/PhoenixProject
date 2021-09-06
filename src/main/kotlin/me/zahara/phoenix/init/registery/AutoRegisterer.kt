package me.zahara.phoenix.init.registery

import me.zahara.phoenix.modId
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.IForgeRegistryEntry

abstract class AutoRegisterer<T: IForgeRegistryEntry<T>>(private val registry : IForgeRegistry<T>, private val modid : String = modId)
{
    private val data : MutableList<Pair<ResourceLocation, () -> T>> = mutableListOf()
    lateinit var entries : List<RegisterEntryProperty<T>>
        private set
    protected open fun register(name : String, value : () -> T) : RegisterEntryProperty<T> = register(ResourceLocation(modid, name), value)

    protected open fun register(name : ResourceLocation, value : () -> T) : RegisterEntryProperty<T>
    {
        data.add(Pair(name, value))
        return RegisterEntryProperty { registry.getValue(name) ?: throw Exception("Unregistered value $name exception") }
    }

    fun register(bus : IEventBus) = bus.addListener { event : RegistryEvent.Register<T> ->
        if (event.registry === registry)
        {
            val entries = mutableListOf<RegisterEntryProperty<T>>()
            for ((location, getter) in data)
            {
                val value = getter()
                value.registryName = location
                event.registry.register(value)
                entries.add(RegisterEntryProperty { registry.getValue(location) ?: throw Exception("Unregistered value $location exception") })
            }
            this.entries = entries
        }
    }
}