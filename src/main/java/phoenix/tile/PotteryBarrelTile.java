package phoenix.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import phoenix.init.PhoenixTiles;

public class PotteryBarrelTile extends TileEntity
{
    public int jumpsCount = 0;
    public PotteryBarrelTile()
    {
        super(PhoenixTiles.POTTERY_BARREL.get());
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        jumpsCount = compound.getInt("jumpscount");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        compound.putInt("jumpscount", jumpsCount);
        return super.write(compound);
    }
    public void incrementJumpsCount()
    {
        jumpsCount++;
        jumpsCount = Math.min(jumpsCount, 1000);
    }
    public void nullifyJumpsCount()
    {
        jumpsCount = 0;
    }
}
