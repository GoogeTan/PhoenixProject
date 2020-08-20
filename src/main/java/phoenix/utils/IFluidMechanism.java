package phoenix.utils;

import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IFluidMechanism
{
    int getNumberInGraph();
    void setNumberInGraph(int number_in_graph);
    FluidTank getInput();
    FluidTank getOutput();
    boolean isEndOrStart();
}
