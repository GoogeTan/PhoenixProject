package phoenix.utils;

import net.minecraft.nbt.CompoundNBT;

public interface ISerializable
{
    void write(CompoundNBT nbt);
    void read(CompoundNBT nbt);
}
