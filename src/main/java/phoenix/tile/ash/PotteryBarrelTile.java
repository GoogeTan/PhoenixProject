package phoenix.tile.ash;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import phoenix.blocks.ash.PotteryBarrelBlock;
import phoenix.init.PhoenixTiles;
import phoenix.utils.block.PhoenixTile;

import java.io.IOException;

public class PotteryBarrelTile extends PhoenixTile<PotteryBarrelTile> implements IInventory, ITickableTileEntity
{
    public int jumpsCount = 0;
    public PotteryBarrelTile()
    {
        super(PhoenixTiles.getPOTTERY_BARREL());
    }

    @Override
    public void tick()
    {
        if(!inventory.isEmpty())
        {
            if(inventory.getItem() == Items.CLAY)
            {
                if(world.getBlockState(pos).get(PotteryBarrelBlock.Companion.getState()) == 1)
                {
                    world.setBlockState(pos, world.getBlockState(pos).with(PotteryBarrelBlock.Companion.getState(), 2));
                }
            }
            else if(inventory.getItem() == Items.WATER_BUCKET)
            {
                if(world.getBlockState(pos).get(PotteryBarrelBlock.Companion.getState()) == 0)
                {
                    world.setBlockState(pos, world.getBlockState(pos).with(PotteryBarrelBlock.Companion.getState(), 1));
                }
            }
            else
            {
                InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY() + 1, pos.getZ(), inventory);
                inventory = ItemStack.EMPTY;
            }
        }
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

    private ItemStack inventory = ItemStack.EMPTY;

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new UpdatePacket(jumpsCount);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        jumpsCount = ((UpdatePacket) pkt).jumpsCount;
    }

    @Override
    public int getSizeInventory()
    {
        return 1;
    }

    @Override
    public boolean isEmpty()
    {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return inventory;
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        ItemStack stack = inventory.copy();
        stack.shrink(count);
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return inventory = ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        inventory = stack;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player)
    {
        return true;
    }

    @Override
    public void clear()
    {
        inventory = ItemStack.EMPTY;
    }

    static class UpdatePacket extends SUpdateTileEntityPacket
    {
        int jumpsCount;

        public UpdatePacket(int jumpsCount)
        {
            this.jumpsCount = jumpsCount;
        }

        @Override
        public void readPacketData(PacketBuffer buf) throws IOException
        {
            super.readPacketData(buf);
            jumpsCount = buf.readInt();
        }

        @Override
        public void writePacketData(PacketBuffer buf) throws IOException
        {
            super.writePacketData(buf);
            buf.writeInt(jumpsCount);
        }
    }
}