package phoenix.tile.ash

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.ITickableTileEntity
import phoenix.api.tile.PhoenixTile
import phoenix.blocks.ash.OvenBlock
import phoenix.blocks.ash.OvenData
import phoenix.init.PhxItems
import phoenix.init.PhxTiles
import phoenix.network.SyncOvenPacket
import phoenix.network.sendToAllPlayers
import phoenix.network.sendToDimension
import phoenix.other.get
import phoenix.other.set
import phoenix.recipes.OvenRecipe.Companion.recipesByResult

open class OvenTile(open val data : OvenData = OvenData(5)) : PhoenixTile(PhxTiles.oven), ITickableTileEntity, IInventory by data
{
    fun getOutOtherItems(): List<ItemStack>
    {
        val res = ArrayList<ItemStack>()
        var has = false
        for (i in 0..3)
        {
            if (recipesByResult.containsKey(data[i].item) || data[i].item == PhxItems.COOKED_SETA)
            {
                res.add(data[i].copy())
                data[i] = ItemStack.EMPTY
                has = true
            }
        }
        if (has)
            SyncOvenPacket(this).sendToAllPlayers()
        return res
    }

    fun getOtherItems(): List<ItemStack>
    {
        val res = ArrayList<ItemStack>()
        for (i in 0..3)
        {
            if (recipesByResult.containsKey(data[i].item) || data[i].item == PhxItems.COOKED_SETA)
            {
                res.add(data[i]);
            }
        }
        return res
    }

    fun addItem(stack: ItemStack): Boolean
    {
        for (i in 0..3)
            if (data[i].isEmpty)
            {
                data[i] = ItemStack(stack.item)
                return true
            }
        return false
    }

    override fun tick()
    {
        val world = world!!
        if (!world.isRemote)
        {
            if (data.isBurning())
            {
                data.tickFire()
                if (world[pos, OvenBlock.STATE] == 2)
                {
                    val hasChanges = data.tickItems()
                    if (hasChanges)
                        SyncOvenPacket(this).sendToDimension(world.dimension.type)
                }
                if (!data.isBurning())
                    world[pos, OvenBlock.STATE] = 0
            }
        }
    }

    override fun write(compound: CompoundNBT): CompoundNBT?
    {
        compound.put("data", data.serializeNBT())
        return super.write(compound)
    }


    override fun read(compound: CompoundNBT)
    {
        data.deserializeNBT(compound.getCompound("data"))
        super.read(compound)
    }


    override fun writePacketData(buf: PacketBuffer)
    {
        super.writePacketData(buf)
        data.writeToBuf(buf)
    }

    override fun readPacketData(buf: PacketBuffer)
    {
        super.readPacketData(buf)
        data.readFromBuf(buf)
    }

    override fun markDirty()
    {
        super.markDirty()
        data.markDirty()
    }
}