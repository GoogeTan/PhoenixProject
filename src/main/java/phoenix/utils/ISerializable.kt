package phoenix.utils

import net.minecraft.nbt.CompoundNBT

interface ISerializable
{
    fun write(nbt: CompoundNBT)
    fun read(nbt: CompoundNBT)
}