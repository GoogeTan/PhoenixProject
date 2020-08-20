package phoenix.tile;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import phoenix.init.PhoenixTile;
import phoenix.utils.IFluidMechanism;

public class PipeTile extends TileEntity implements IFluidMechanism
{
    int number_in_graph;
    public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 5);
    public PipeTile()
    {
        super(PhoenixTile.PIPE.get());
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
        return false;
    }

    public World getWorld()
    {
        return world;
    }
    public BlockState getBlockState()
    {
        return world.getBlockState(pos);
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
}
