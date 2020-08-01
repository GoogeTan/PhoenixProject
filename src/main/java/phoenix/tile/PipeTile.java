package phoenix.tile;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import phoenix.init.PhoenixTile;
import phoenix.utils.IFluidMechanism;

public class PipeTile extends TileEntity implements IFluidMechanism
{
    public FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME * 5);
    public PipeTile()
    {
        super(PhoenixTile.PIPE.get());
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

    public World getWorld()
    {
        return world;
    }
    public BlockState getBlockState()
    {
        return world.getBlockState(pos);
    }
}
