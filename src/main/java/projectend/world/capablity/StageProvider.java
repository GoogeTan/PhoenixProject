package projectend.world.capablity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import static projectend.ProjectEnd.STAGER_CAPABILITY;

public class StageProvider implements ICapabilitySerializable<NBTTagCompound>
{
    IStager instance = STAGER_CAPABILITY.getDefaultInstance();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == STAGER_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        return hasCapability(capability, facing) ? STAGER_CAPABILITY.<T>cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        return (NBTTagCompound) STAGER_CAPABILITY.getStorage().writeNBT(STAGER_CAPABILITY, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        STAGER_CAPABILITY.getStorage().readNBT(STAGER_CAPABILITY, instance, null, nbt);
    }
}


