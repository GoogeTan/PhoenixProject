package phoenix.tile.ash;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryManager;
import phoenix.blocks.ash.PotteryBarrelBlock;
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
        jumpsCount = Math.min(jumpsCount, 200);
    }

    public void nullifyJumpsCount()
    {
        jumpsCount = 0;
    }
}
