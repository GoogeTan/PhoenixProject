package phoenix.utils.pipe;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import phoenix.utils.graph.GraphNode;

import java.util.ArrayList;

public interface IFluidMechanism extends IFluidPipe
{
    FluidTank getInput();
    FluidTank getOutput();
    void removeMechanismByIndex(int index);
    void addMechanismByIndex(ServerWorld world, int index);
    Pair<ArrayList<GraphNode>, ArrayList<GraphNode>> getConnectedMechanisms(int numberInGraph);
}
