package phoenix.tile.redo

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import phoenix.blocks.ash.PotteryBarrelBlock
import phoenix.init.PhxTiles
import phoenix.utils.block.PhoenixTile
import phoenix.utils.get
import phoenix.utils.set

class ElectricBarrelTile : PhoenixTile(PhxTiles.electricBarrel), IInventory by Inventory(1), ITickableTileEntity
{
    var jumpsCount = 0
    var stack : ItemStack
        get() = getStackInSlot(0)
        set(value) = setInventorySlotContents(0, value)

    override fun tick()
    {
        val world = world!!
        if (!stack.isEmpty)
        {
            if (stack.item === Items.CLAY)
            {
                if (world[pos].get(PotteryBarrelBlock.POTTERY_STATE) == 1)
                {
                    world[pos, PotteryBarrelBlock.POTTERY_STATE] = 2
                }
            } else if (stack.item === Items.WATER_BUCKET)
            {
                if (world[pos, PotteryBarrelBlock.POTTERY_STATE] == 0)
                {
                    world[pos, PotteryBarrelBlock.POTTERY_STATE] = 1
                }
            } else
            {
                InventoryHelper.spawnItemStack(
                    world,
                    pos.x.toDouble(),
                    (pos.y + 1).toDouble(),
                    pos.z.toDouble(),
                    stack
                )
                stack = ItemStack.EMPTY
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
    }

    override fun readPacketData(buf: PacketBuffer)
    {
        super.readPacketData(buf)
        jumpsCount = buf.readInt()
        stack = buf.readItemStack()
    }

    override fun writePacketData(buf: PacketBuffer)
    {
        super.writePacketData(buf)
        buf.writeInt(jumpsCount)
        buf.writeItemStack(stack)
    }
}