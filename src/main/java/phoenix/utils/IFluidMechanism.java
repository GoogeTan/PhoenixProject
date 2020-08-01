package phoenix.utils;

import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IFluidMechanism
{
    FluidTank getInput();
    FluidTank getOutput();
}
