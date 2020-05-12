package projectend.world.capablity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class StageStorage implements Capability.IStorage<IStager>
{
    @Override
    public NBTBase writeNBT (Capability<IStager> capability, IStager instance, EnumFacing side)
    {
        final NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("projectendstage", instance.getStage());
        return tag;
    }

    @Override
    public void readNBT (Capability<IStager> capability, IStager instance, EnumFacing side, NBTBase nbt)
    {
        final NBTTagCompound tag = (NBTTagCompound) nbt;
        instance.setStage(tag.getInteger("projectendstage"));
    }
}

