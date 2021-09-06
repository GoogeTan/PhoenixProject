package me.zahara.phoenix.init.registery

import net.minecraftforge.registries.IForgeRegistryEntry
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class RegisterEntryProperty<out T : IForgeRegistryEntry<out T>>(getter : () -> T) : ReadOnlyProperty<Any?, T>
{
    private val data by lazy(getter)

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = data
}