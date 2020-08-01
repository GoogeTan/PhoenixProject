package phoenix.world.capa;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class StageStorage implements Capability.IStorage<IStager>
{
    @Nullable
    @Override
    public INBT writeNBT(Capability<IStager> capability, IStager instance, Direction side)
    {
        final CompoundNBT tag = new CompoundNBT();
        tag.putInt("phoenixstage", instance.getStage());
        tag.putInt("phoenixstagein", instance.getStageIn());
        return tag;

    }

    @Override
    public void readNBT(Capability<IStager> capability, IStager instance, Direction side, INBT nbt)
    {
        final CompoundNBT tag = (CompoundNBT) nbt;
        instance.setStage(tag.getInt("phoenixstage"));
        instance.setStageIn(tag.getInt("phoenixstagein"));
    }
}