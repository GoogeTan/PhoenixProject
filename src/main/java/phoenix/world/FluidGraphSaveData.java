package phoenix.world;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import phoenix.Phoenix;
import phoenix.utils.ArrayUtils;
import phoenix.utils.block.IFluidMechanism;

import java.util.ArrayList;
import java.util.HashSet;

public class FluidGraphSaveData extends WorldSavedData
{
    private static final String DATA_NAME = "phoenix_fluid";
    private ArrayList<HashSet<Integer>> graph = new ArrayList();
    private ArrayList<Pair<Boolean, BlockPos>> in_out_puts = new ArrayList();

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
        graph.add(new HashSet<>());
        in_out_puts.add(Pair.of(is_inout_put, (is_inout_put ? pos : null)));
        for (Direction dir: Direction.values())
        {
            if(world.getTileEntity(pos.offset(dir)) instanceof IFluidMechanism)
            {
                graph.get(tile.getNumberInGraph()).add(((IFluidMechanism) world.getTileEntity(pos.offset(dir))).getNumberInGraph());
                graph.get(((IFluidMechanism) world.getTileEntity(pos.offset(dir))).getNumberInGraph()).add(tile.getNumberInGraph());
            }
        }
        Phoenix.LOGGER.error(graph.size());
    }

    private ArrayList<Integer> colorTmp = new ArrayList<>();
    private ArrayList<BlockPos> tmp = new ArrayList<>();

    public ArrayList<BlockPos> getInputs(int numberInGraph)
    {
        tmp.clear();
        colorTmp.clear();
        ArrayUtils.resize(colorTmp, graph.size(), 0);
        bfs(numberInGraph, 0, 25);
        return tmp;
    }

    private void bfs(int v, int color, int maxColor)
    {
        colorTmp.set(v, color);
        if(in_out_puts.get(v).getFirst())
        {
            tmp.add(in_out_puts.get(v).getSecond());
        }
        if(color < maxColor)
            for (int i : graph.get(v))
                bfs(i, color + 1, maxColor);
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

    @Override
    public String toString()
    {
        String res = "Fluid graph: \n";
        for (int i = 0; i < graph.size(); ++i)
        {
            res += i;
            res += ": ";
            for (int j : graph.get(i))
            {
                res += j;
                res += " ";
            }
            res += "\n";
        }
        return res;
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
