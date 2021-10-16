package me.zahara.phoenix.init.registery

import net.minecraftforge.registries.IForgeRegistryEntry
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class RegisterEntryProperty<T, Type : IForgeRegistryEntry<T>>(getter : () -> Type) : ReadOnlyProperty<Any?, Type>
{
    private val data by lazy(getter)

    override fun getValue(thisRef: Any?, property: KProperty<*>): Type = data
}