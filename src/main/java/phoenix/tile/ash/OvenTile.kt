package phoenix.tile.ash

import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.util.NonNullList
import phoenix.init.PhoenixItems
import phoenix.init.PhoenixTiles
import phoenix.recipes.OvenRecipe.recipes_from_inputs
import phoenix.utils.block.PhoenixTile
import java.lang.Integer.max
import java.lang.Integer.min

class OvenTile : PhoenixTile(PhoenixTiles.OVEN.get()), ITickableTileEntity
{
    var timers = IntArray(4)
    var burnTime = 0
    val maxBurnTime = 20 * 60
    val inventory: NonNullList<ItemStack> = NonNullList.withSize(4, ItemStack.EMPTY)
    init
    {
        timers[0] = -1
        timers[1] = -1
        timers[2] = -1
        timers[3] = -1
    }

    fun outOtherItems() : List<ItemStack>
    {
        val res = ArrayList<ItemStack>()
        for (i in 0 until inventory.size)
        {
            if(!recipes_from_inputs.contains(inventory[i].item) || inventory[i].item == PhoenixItems.COOKED_SETA)
            {
                res.add(inventory[i].copy())
                inventory[i] = ItemStack.EMPTY
            }
        }
        return res
    }

    fun getOtherItems() : List<ItemStack>
    {
        val res = ArrayList<ItemStack>()
        for (i in 0 until inventory.size)
        {
            if(!recipes_from_inputs.contains(inventory[i].item))
            {
                res.add(inventory[i]);
            }
        }
        return res
    }

    fun addItem(stack : ItemStack) : Boolean
    {
        for (i in 0..3)
            if(inventory[i].item == Items.AIR)
            {
                inventory[i] = stack
                timers[i] = 0
                return true
            }
        return false
    }

    override fun tick()
    {
        if(!world!!.isRemote)
        {
            burnTime = min(maxBurnTime, burnTime)
            burnTime--
            burnTime = max(0, burnTime)
            for (i in 0 until inventory.size)
            {
                val current = inventory[i]
                if (recipes_from_inputs.contains(current.item))
                {
                    val recipe = recipes_from_inputs[current.item]!!
                    val cookTime: Int = recipe.cookTime
                    if(burnTime != 0)
                        if (timers[i] != 0)
                            timers[i]--
                    else
                        timers[i]++
                    if (timers[i] >= cookTime)
                    {
                        inventory[i] = recipe.result
                    }
                }
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
        val nbt = compound.getCompound("container")
        for (i in 0..3)
        {
            val current = nbt.getCompound("slot$i")
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