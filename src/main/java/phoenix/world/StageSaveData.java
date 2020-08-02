package phoenix.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import phoenix.Phoenix;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StageSaveData extends WorldSavedData
{
    private static final String DATA_NAME = "phoenix_stage";
    private CompoundNBT data;

    public StageSaveData()
    {
        super(DATA_NAME);
        this.data = new CompoundNBT();
        this.data.putInt("stage", 1);
        this.data.putInt("part", 1);
    }

    //Эт второй конструктор, на всякий. А первый нужен для того, чтобы ничего не упало)
    public StageSaveData(String s)
    {
        super(s);
        this.data = new CompoundNBT();
        this.data.putInt("stage", 1);
        this.data.putInt("part", 1);
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        if (nbt.contains("stage_nbt"))
        {
            data = nbt.getCompound("stage_nbt");
        }
        else
        {
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("stage", 1);
            compound.putInt("part",  1);
            data = compound;
            nbt.put("stage_nbt", compound);
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.put("stage_nbt", data);
        return compound;
    }

    //Этим мы получаем экземпляр данных для мира
    @Nonnull
    public static StageSaveData get(ServerWorld world)
    {
        DimensionSavedDataManager storage = world.getSavedData();
        StageSaveData instance = storage.getOrCreate(StageSaveData::new, DATA_NAME);
        if (instance == null)
        {
            instance = new StageSaveData();
            storage.set(instance);
        }
        return instance;
    }

    public int getStage()
    {
        if(data == null) data = new CompoundNBT();
        int s = data.getInt("stage");
        if(s == 0)
        {
            data.putInt("stage", 1);
            return 1;
        }
        else
        {
            return s;
        }
    }
    public int getPart()
    {
        if(data == null) data = new CompoundNBT();
        int s = data.getInt("part");
        if(s == 0)
        {
            data.putInt("part", 1);
            return 1;
        }
        else
        {
            return s;
        }
    }
    public void setStage(int stage)
    {
        data.putInt("stage", stage);
        markDirty();
    }
    public void setPart(int part)
    {
        data.putInt("part", part);
        markDirty();
    }
    public void addStage()
    {
        data.putInt("stage", getStage() + 1);
        markDirty();
    }
    public void addPart()
    {
        data.putInt("part", getPart() + 1);
        markDirty();
    }
}
