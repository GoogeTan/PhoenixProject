package phoenix.world;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import phoenix.utils.IFluidMechanism;

import java.util.ArrayList;
import java.util.HashSet;

public class FluidGraphSaveData extends WorldSavedData
{
    private static final String DATA_NAME = "phoenix_fluid";
    private ArrayList<HashSet<Integer>> graph = new ArrayList();
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
                graph.get(((IFluidMechanism) world.getTileEntity(pos.offset(dir))).getNumberInGraph()).add(tile.getNumberInGraph());
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
                graph.add(i, new HashSet<>(list));
            }
        }
        else
        {
            data = new CompoundNBT();
            updateData();
        }
    }

      
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
            this.data.putIntArray("graph_part_" + i, ImmutableList.copyOf(graph.get(i)));
    }
    //Этим мы получаем экземпляр данных для мира
      
    public static FluidGraphSaveData get(ServerWorld world)
    {
        return world.getSavedData().getOrCreate(FluidGraphSaveData::new, DATA_NAME);
    }
}
