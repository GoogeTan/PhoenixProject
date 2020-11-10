package phoenix.utils;

import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class GraphNode
{
    public final BlockPos pos;
    int distance;
    public final boolean isInput, isOutput;
    public final ArrayList<Integer> path;
    public GraphNode(BlockPos pos, int distance, boolean isInput, boolean isOutput)
    {
        this.pos = pos;
        this.distance = distance;
        this.isInput = isInput;
        this.isOutput = isOutput;
        this.path = new ArrayList<>();
    }

    public GraphNode(BlockPos pos, int distance, ArrayList<Integer> path, boolean isInput, boolean isOutput)
    {
        this.pos = pos;
        this.distance = distance;
        this.isInput = isInput;
        this.isOutput = isOutput;
        this.path = path;
    }

    public GraphNode(BlockPos pos, int distance)
    {
        this.pos = pos;
        this.distance = distance;
        this.isInput = false;
        this.isOutput = false;
        this.path = new ArrayList<>();
    }

    @Override
    public String toString()
    {
        String s = "GraphNode { distance=";
        s += distance;
        s += " pos=";
        s += pos;
        s += " path=";
        for (Integer integer : path)
        {
            s += integer;
            s += " ";
        }
        s += "}";
        return s;
    }
}
