package phoenix.tile.ash

import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import phoenix.containers.OvenInventory
import phoenix.init.PhoenixTiles
import phoenix.recipes.OvenRecipe
import phoenix.utils.block.PhoenixTile

class OvenTile : PhoenixTile(PhoenixTiles.OVEN.get()), ITickableTileEntity
{
    var timers = IntArray(4)
    var inventory: OvenInventory = OvenInventory(4)
    var burnTime = 0
    val maxBurnTime = 20 * 4

    init
    {
        timers[0] = 0
        timers[1] = 0
        timers[2] = 0
        timers[3] = 0
    }
    
    override fun tick()
    {
        val slotList = inventory
        for (i in 0..3)
        {
            val current = slotList[i]
            val recipe = OvenRecipe.recipes_from_inputs[current.stack.item]
            if (recipe != null)
            {
                val cookTime = recipe.cookTime
                timers[i]++
                if (timers[i] >= cookTime)
                {
                    inventory[i] = recipe.result
                }
            } else
            {
                timers[i] = 0
            }
        }
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket = UpdatePacket(timers, burnTime)

    override fun onDataPacket(net: NetworkManager?, pkt: SUpdateTileEntityPacket?)
    {
        timers = (pkt as UpdatePacket).timers
        burnTime = pkt.burnTime
    }

    override fun write(compound: CompoundNBT): CompoundNBT?
    {
        compound.putIntArray("timers", timers)
        compound.putInt("burn_time", burnTime)


        val nbt = CompoundNBT()
        for (i in 0..3)
        {
            val stack = CompoundNBT()
            inventory[i].write(stack)
            nbt.put("slot$i", stack)
        }
        compound.put("container", nbt)

        return super.write(compound)
    }


    override fun read(compound: CompoundNBT)
    {
        timers = compound.getIntArray("timers")
        burnTime = compound.getInt("burn_time")
        for (i in 0..3)
        {
            val current = compound.getCompound("slot$i")
            inventory[i] = ItemStack.read(current)
        }
        super.read(compound)
    }


    class UpdatePacket(var timers: IntArray, var burnTime : Int) : SUpdateTileEntityPacket()
    {
        override fun writePacketData(buf: PacketBuffer)
        {
            super.writePacketData(buf)
            buf.writeVarIntArray(timers)
            buf.writeVarInt(burnTime)
        }

        override fun readPacketData(buf: PacketBuffer)
        {
            super.readPacketData(buf)
            timers = buf.readVarIntArray()
            burnTime = buf.readInt()
        }
    }
}