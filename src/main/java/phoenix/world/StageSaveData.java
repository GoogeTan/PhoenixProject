package phoenix.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;
import java.io.File;

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

    //Этj второй конструктор, на всякий. А первый нужен для того, чтобы ничего не упало)
    public StageSaveData(String s)
    {
        super(s);
        this.data = new CompoundNBT();
        this.data.putInt("stage", 0);
        this.data.putInt("part", 0);
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
            compound.putInt("stage", 0);
            compound.putInt("part",  0);
            data = compound;
            nbt.put("stage_nbt", compound);
        }
    }

      
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.put("stage_nbt", data);
        return compound;
    }

    //Этим мы получаем экземпляр данных для мира
    //Безопасен в любое время - длаже если WSD еще не инициализирована.
    public static StageSaveData get(@Nonnull ServerWorld world)
    {
        try
        {
            return world.getSavedData().getOrCreate(StageSaveData::new, DATA_NAME);
        }
        catch (Exception ignored)
        {
            try
            {
                String saveName = "";
                SaveHandler saveHandler = world.getServer().getActiveAnvilConverter().getSaveLoader(saveName, world.getServer());
                File file1 = DimensionType.THE_END.getDirectory(saveHandler.getWorldDirectory());
                File file2 = new File(file1, "data");
                file2.mkdirs();
                return new DimensionSavedDataManager(file2, null).getOrCreate(StageSaveData::new, DATA_NAME);   
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
                return null;
            }
        }
    }

    public int getStage()
    {
        if(data == null) data = new CompoundNBT();
        return data.getInt("stage");
    }

    public int getPart()
    {
        if(data == null) data = new CompoundNBT();
        return data.getInt("part");
    }

    private void setStage(int stage)
    {
        data.putInt("stage", stage);
        markDirty();
    }

    private void setPart(int part)
    {
        data.putInt("part", part);
        markDirty();
    }

    public void addStage()
    {
        setStage(Math.min(getStage() + 1, 3));
        markDirty();
    }

    public void addPart()
    {
        setPart(getPart() + 1);
        if(data.getInt("part") >= 3)
        {
            addStage();
            setPart(0);
        }
        markDirty();
    }
}
