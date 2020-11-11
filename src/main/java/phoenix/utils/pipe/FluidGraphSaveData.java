package phoenix.utils.pipe;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import phoenix.utils.GraphNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class FluidGraphSaveData extends WorldSavedData
{
    private static final String DATA_NAME = "phoenix_fluid";
    /*
     *Contains graph elements.
     */
    protected ArrayList<GraphNode> elements = new ArrayList();
    /*
     *Contains graph element neighbors.
     */
    protected ArrayList<HashSet<Integer>> graph = new ArrayList();


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

    public void addBlock(IWorld world, BlockPos pos, boolean isInput, boolean isOutput)
    {
        IFluidMechanism tile = ((IFluidMechanism)world.getTileEntity(pos));
        tile.setNumberInGraph(graph.size());
        graph.add(new HashSet<>());
        elements.add(new GraphNode(pos, 0, isInput, isOutput));
        for (Direction dir: Direction.values())
        {
            if(world.getTileEntity(pos.offset(dir)) instanceof IFluidMechanism)
            {
                graph.get(tile.getNumberInGraph()).add(((IFluidMechanism) world.getTileEntity(pos.offset(dir))).getNumberInGraph());
                graph.get(((IFluidMechanism) world.getTileEntity(pos.offset(dir))).getNumberInGraph()).add(tile.getNumberInGraph());
            }
        }
    }

    private Integer[] colorTmp;
    private final ArrayList<GraphNode> outputsTmp = new ArrayList<>();
    private final ArrayList<GraphNode> inputsTmp = new ArrayList<>();

    public Pair<ArrayList<GraphNode>, ArrayList<GraphNode>> findConnectedMechanisms(int numberInGraph)
    {
        outputsTmp.clear();
        inputsTmp.clear();
        colorTmp = new Integer[graph.size()];
        Arrays.fill(colorTmp, 0);
        findMechanisms(numberInGraph, 0, 25, new ArrayList<>());
        return Pair.of((ArrayList<GraphNode>) inputsTmp.clone(), (ArrayList<GraphNode>) outputsTmp.clone());
    }

    /*
     * It is simply BFS
     */
    private void findMechanisms(int v, int color, int maxColor, ArrayList<Integer> path)
    {
        path.add(v);
        colorTmp[v] = color;
        if(elements.get(v).isOutput)
            outputsTmp.add(new GraphNode(elements.get(v).pos, color, path, elements.get(v).isInput, elements.get(v).isOutput));
        if(elements.get(v).isInput)
             inputsTmp.add(new GraphNode(elements.get(v).pos, color, path, elements.get(v).isInput, elements.get(v).isOutput));

        if(color < maxColor)
            for (int i : graph.get(v))
                if(colorTmp[i] != 0)
                    findMechanisms(i, color + 1, maxColor, path);
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
                    list.add(j);

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
