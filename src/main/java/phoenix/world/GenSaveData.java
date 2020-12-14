package phoenix.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

public class GenSaveData extends WorldSavedData
{
    private static final String DATA_NAME = "phoenix_gen";
    private CompoundNBT data;

    public GenSaveData()
    {
        super(DATA_NAME);
        this.data = new CompoundNBT();
        this.data.putBoolean("iscorngened", false);
    }

    //Это второй конструктор, на всякий. А первый нужен для того, чтобы ничего не упало)
    public GenSaveData(String s)
    {
        super(s);
        this.data = new CompoundNBT();
        this.data.putBoolean("iscorngened", false);
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        if (nbt.contains("gen_nbt"))
        {
            data = nbt.getCompound("gen_nbt");
        }
        else
        {
            CompoundNBT compound = new CompoundNBT();
            this.data.putBoolean("iscorngened", false);
            data = compound;
            nbt.put("gen_nbt", compound);
        }
    }


    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.put("gen_nbt", data);
        return compound;
    }

    //Этим мы получаем экземпляр данных для мира
    public static GenSaveData get(ServerWorld world)
    {
        return world.getSavedData().getOrCreate(GenSaveData::new, DATA_NAME);
    }

    public boolean isCornGenned()
    {
        if(data == null) data = new CompoundNBT();
        return data.getBoolean("iscorngened");
    }

    public void setCornGenned()
    {
        data.putBoolean("iscorngened", true);
        markDirty();
    }
}
