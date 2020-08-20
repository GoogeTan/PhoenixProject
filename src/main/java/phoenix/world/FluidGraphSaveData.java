package phoenix.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;
import phoenix.tile.PipeTile;
import phoenix.utils.IFluidMechanism;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class FluidGraphSaveData extends WorldSavedData
{
    private static final String DATA_NAME = "phoenix_fluid";
    private ArrayList<ArrayList<Integer>> graph = new ArrayList();
    private ArrayList<Pair<Boolean, BlockPos>> inout_puts = new ArrayList();
    private CompoundNBT data;

    public FluidGraphSaveData()
    {
        super(DATA_NAME);
        this.data = new CompoundNBT();
        updateData();
    }

    //Эт второй конструктор, на всякий. А первый нужен для того, чтобы ничего не упало)
    public FluidGraphSaveData(String s)
    {
        super(s);
        this.data = new CompoundNBT();
        updateData();
    }

    public void addBlock(IWorld world, BlockPos pos, boolean is_inout_put)
    {
        IFluidMechanism tile = ((IFluidMechanism)world.getTileEntity(pos));
        tile.setNumberInGraph(graph.size());
        inout_puts.add(Pair.of(is_inout_put, (is_inout_put ? pos : null)));
        for (Direction dir: Direction.values())
        {
            if(world.getTileEntity(pos.offset(dir)) instanceof IFluidMechanism)
            {
                graph.get(tile.getNumberInGraph()).add(((IFluidMechanism) world.getTileEntity(pos.offset(dir))).getNumberInGraph());
            }
        }
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        if (nbt.contains("graph"))
        {
            data = nbt.getCompound("graph");
            int size = data.getInt("graph_size");
            for (int i = 0; i < size; i++)
            {
                ArrayList<Integer> list = new ArrayList<>();
                int[] arr = data.getIntArray("graph_part_" + 1);
                for (int j : arr)
                {
                    list.add(j);
                }
                graph.add(i, new ArrayList<>(list));
            }
        }
        else
        {
            data = new CompoundNBT();
            updateData();
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        updateData();
        compound.put("graph", data);
        return compound;
    }

    public void updateData()
    {
        data.putInt("graph_size", graph.size());
        for (int i = 0; i < graph.size(); i++)
        {
            this.data.putIntArray("graph_part_" + i, graph.get(i));
        }
    }

    //Этим мы получаем экземпляр данных для мира
    @Nonnull
    public static FluidGraphSaveData get(ServerWorld world)
    {
        DimensionSavedDataManager storage = world.getSavedData();
        FluidGraphSaveData instance = storage.getOrCreate(FluidGraphSaveData::new, DATA_NAME);
       /* if (instance == null)
        {
            instance = new FluidGraphSaveData();
            storage.set(instance);
        }*/
        return instance;
    }
}
