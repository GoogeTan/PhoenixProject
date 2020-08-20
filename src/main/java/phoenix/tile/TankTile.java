package phoenix.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import phoenix.init.PhoenixTile;
import phoenix.utils.IFluidMechanism;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TankTile extends TileEntity implements IFluidMechanism
{
    int number_in_graph;
    public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 5);

    private final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public TankTile()
    {
        super(PhoenixTile.TANK.get());
    }

    @Override
    public void read(CompoundNBT tag)
    {
        super.read(tag);
        tank.readFromNBT(tag);
        number_in_graph = tag.getInt("number_in_graph");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        tag = super.write(tag);
        tank.writeToNBT(tag);
        tag.putInt("number_in_graph", number_in_graph);
        return tag;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public int getNumberInGraph()
    {
        return number_in_graph;
    }

    @Override
    public void setNumberInGraph(int number_in_graph)
    {
        this.number_in_graph = number_in_graph;
    }

    @Override
    public FluidTank getInput()
    {
        return tank;
    }

    @Override
    public FluidTank getOutput()
    {
        return tank;
    }

    @Override
    public boolean isEndOrStart()
    {
        return true;
    }
}