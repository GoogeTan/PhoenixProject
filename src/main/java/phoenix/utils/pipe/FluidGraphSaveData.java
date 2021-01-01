package phoenix.utils.pipe;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import phoenix.utils.SizedArrayList;
import phoenix.utils.graph.GraphNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class FluidGraphSaveData extends WorldSavedData
{
    private static final String DATA_NAME = "phoenix_fluid";
    /*
     *Contains graph elements.
     */
    public ArrayList<GraphNode> elements = new ArrayList();
    /*
     *Contains graph element neighbors.
     */
    public ArrayList<HashSet<Integer>> graph = new ArrayList();

    /*
     *Contains graph element indexes which has been removed.
     */
    protected LinkedList<Integer> removedIndexes = new LinkedList<>();


    private CompoundNBT data;

    public FluidGraphSaveData()
    {
        super(DATA_NAME);
        this.data = new CompoundNBT();
        updateData();
    }

    public FluidGraphSaveData(String s)
    {
        super(s);
        this.data = new CompoundNBT();
        updateData();
    }

    public void addBlock(ServerWorld world, BlockPos pos, boolean isInput, boolean isOutput)
    {
        IFluidPipe tile = ((IFluidPipe)world.getTileEntity(pos));
        int index = graph.size();
        if(!removedIndexes.isEmpty())
        {
            index = removedIndexes.getFirst();
            removedIndexes.remove(index);
        }
        tile.setNumberInGraph(index);
        if(index != elements.size())
        {
            graph.set(index, new HashSet<>());
            elements.set(index, new GraphNode(world, pos, isInput, isOutput));
        }
        else
        {
            graph.add(new HashSet<>());
            elements.add(new GraphNode(world, pos, isInput, isOutput));
        }

        for (Direction dir: Direction.values())
        {
            if(world.getTileEntity(pos.offset(dir)) != null && world.getTileEntity(pos.offset(dir)) instanceof IFluidMechanism)
            {
                graph.get(tile.getNumberInGraph()).add(((IFluidMechanism) world.getTileEntity(pos.offset(dir))).getNumberInGraph());
                graph.get(((IFluidMechanism) world.getTileEntity(pos.offset(dir))).getNumberInGraph()).add(tile.getNumberInGraph());
            }
        }
        for(GraphNode node : elements)
        {
            if(node.pipe instanceof IFluidMechanism)
                ((IFluidMechanism) node.pipe).addMechanismByIndex(world, index);
        }
    }

    public void removeBlock(int numberInGraph)
    {
        assert (numberInGraph < graph.size() && numberInGraph > 0);

        for (int i : graph.get(numberInGraph))
            graph.get(i).remove(numberInGraph);

        for (GraphNode el : elements)
        {
            TileEntity entity = el.tileEntity;
            if (entity instanceof IFluidMechanism)
            {
                ((IFluidMechanism) entity).removeMechanismByIndex(numberInGraph);
            }
        }
        graph.set(numberInGraph, null);
        removedIndexes.add(numberInGraph);
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
        StringBuilder res = new StringBuilder("Fluid graph: \n");
        for (int i = 0; i < graph.size(); ++i)
        {
            res.append(i);
            res.append(": ");
            for (int j : graph.get(i))
            {
                res.append(j);
                res.append(" ");
            }
            res.append("\n");
        }
        return res.toString();
    }

    public void updateData()
    {
        data.putInt("graph_size", graph.size());
        for (int i = 0; i < graph.size(); i++)
            this.data.putIntArray("graph_part_" + i, SizedArrayList.copyOf(graph.get(i)));
    }
    //Этим мы получаем экземпляр данных для мира
      
    public static FluidGraphSaveData get(ServerWorld world)
    {
        return world.getSavedData().getOrCreate(FluidGraphSaveData::new, DATA_NAME);
    }
}
