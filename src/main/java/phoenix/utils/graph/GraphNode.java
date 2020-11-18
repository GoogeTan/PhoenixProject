package phoenix.utils.graph;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import phoenix.utils.pipe.IFluidPipe;

public class GraphNode
{
    public final BlockPos pos;
    public final IWorld world;
    public final TileEntity tileEntity;
    public final boolean isInput, isOutput;
    public final IFluidPipe pipe;

    public GraphNode(IWorld world, BlockPos pos, boolean isInput, boolean isOutput)
    {
        this.world = world;
        this.pos = pos;
        this.isInput = isInput;
        this.isOutput = isOutput;
        tileEntity = world.getTileEntity(pos);
        pipe = (IFluidPipe) tileEntity;
    }

    public GraphNode(IWorld world, BlockPos pos)
    {
        this.world = world;
        this.pos = pos;
        this.isInput = false;
        this.isOutput = false;
        tileEntity = world.getTileEntity(pos);
        pipe = (IFluidPipe) tileEntity;
    }

    @Override
    public String toString()
    {
        String s = "GraphNode { ";
        s += " pos=";
        s += pos;
        s += "}";
        return s;
    }
}
