package phoenix.utils.graph;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.ArrayList;

public class MGraphNode extends GraphNode
{
    int distance = 0;
    public final ArrayList<Integer> path;
    public MGraphNode(IWorld world, BlockPos pos, int distance, ArrayList<Integer> path,  boolean isInput, boolean isOutput)
    {
        super(world, pos, isInput, isOutput);
       this.distance = distance;
       this.path = path;
    }
}
