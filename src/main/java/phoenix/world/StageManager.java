package phoenix.world;

import net.minecraft.nbt.CompoundNBT;

public class StageManager
{
    private static CompoundNBT data = new CompoundNBT();

    public static void read(CompoundNBT nbt)
    {
        if (nbt.contains("stage_nbt"))
        {
            data = nbt.getCompound("stage_nbt");
        }
        else
        {
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("stage", 0);
            compound.putInt("part",  0);
            data = compound;
            nbt.put("stage_nbt", compound);
        }
    }

    public static CompoundNBT write(CompoundNBT compound)
    {
        compound.put("stage_nbt", data);
        return compound;
    }

    public static int getStage()
    {
        if(data == null) data = new CompoundNBT();
        return data.getInt("stage");
    }

    public static int getPart()
    {
        if(data == null) data = new CompoundNBT();
        return data.getInt("part");
    }

    private static void setStage(int stage)
    {
        data.putInt("stage", stage);
    }

    private static void setPart(int part)
    {
        data.putInt("part", part);
    }

    public static void addStage()
    {
        setStage(Math.min(getStage() + 1, 3));
    }

    public static void addPart()
    {
        setPart(getPart() + 1);
        if(data.getInt("part") >= 3)
        {
            addStage();
            setPart(0);
        }
    }
}
