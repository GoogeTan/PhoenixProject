package phoenix.tile.ash

import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.ITickableTileEntity
import phoenix.api.tile.PhoenixTile
import phoenix.blocks.ash.PotteryBarrelBlock.Companion.POTTERY_STATE
import phoenix.init.PhxTiles.potteryBarrel
import phoenix.other.get
import phoenix.other.set

class PotteryBarrelTile : PhoenixTile(potteryBarrel), IInventory by Inventory(1), ITickableTileEntity
{
    var jumpsCount = 0
    private var inventory
            get() = getStackInSlot(0)
            set(value) = setInventorySlotContents(0, value)

    override fun tick()
    {
        val world = world
        if (world != null && !world.isRemote && !inventory.isEmpty)
        {
            if (inventory.item === Items.CLAY)
            {
                if (world[pos, POTTERY_STATE] == 1)
                {
                    world[pos, POTTERY_STATE] = 2
                }
            }
            else if (inventory.item === Items.WATER_BUCKET)
            {
                if (world[pos, POTTERY_STATE] == 0)
                {
                    world[pos, POTTERY_STATE] = 1
                }
            }
            else
            {
                InventoryHelper.spawnItemStack(
                    world,
                    pos.x.toDouble(),
                    (pos.y + 1).toDouble(),
                    pos.z.toDouble(),
                    inventory
                )
                inventory = ItemStack.EMPTY
            }
        }
    }

    override fun read(compound: CompoundNBT)
    {
        super.read(compound)
        jumpsCount = compound.getInt("jumpscount")
    }

    override fun write(compound: CompoundNBT): CompoundNBT
    {
        compound.putInt("jumpscount", jumpsCount)
        return super.write(compound)
    }

    fun incrementJumpsCount()
    {
        jumpsCount++
        jumpsCount = Math.min(jumpsCount, 200)
    }

    fun nullifyJumpsCount()
    {
        jumpsCount = 0
        removeStackFromSlot(0)
    }

    override fun markDirty()
    {
        super.markDirty()
        (this as IInventory).markDirty()
    }

    override fun readPacketData(buf: PacketBuffer)
    {
        super.readPacketData(buf)
        jumpsCount = buf.readInt()
    }

    override fun writePacketData(buf: PacketBuffer)
    {
        super.writePacketData(buf)
        buf.writeInt(jumpsCount)
    }
}