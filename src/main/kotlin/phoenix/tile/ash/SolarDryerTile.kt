package phoenix.tile.ash

import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntityType
import phoenix.api.tile.TickablePhoenixTile
import phoenix.init.PhxTiles
import phoenix.network.SyncDryerPacket
import phoenix.network.sendToDimension
import phoenix.recipes.DryerRecipe

class SolarDryerTile(tileEntityTypeIn: TileEntityType<out SolarDryerTile>) : TickablePhoenixTile(tileEntityTypeIn)
{
    constructor() : this(PhxTiles.solarDryer)
    var item : ItemStack = ItemStack.EMPTY
    var time : Int = 0

    override fun write(compound: CompoundNBT): CompoundNBT
    {
        val res = super.write(compound)
        res.put("stack", item.write(CompoundNBT()))
        res.putInt("time", time)
        return res
    }

    override fun read(compound: CompoundNBT)
    {
        super.read(compound)
        item = ItemStack.read(compound.getCompound("stack"))
        time = compound.getInt("time")
    }

    override fun readPacketData(buf: PacketBuffer)
    {
        super.readPacketData(buf)
        buf.writeItemStack(item)
        buf.writeInt(time)
    }

    override fun writePacketData(buf: PacketBuffer)
    {
        super.writePacketData(buf)
        item = buf.readItemStack()
        time = buf.readInt()
    }

    private fun sync()
    {
        SyncDryerPacket(pos, item).sendToDimension(world!!.dimension.type)
    }

    override fun serverTick()
    {
        if (world!!.canSeeSky(pos))
        {
            val recipe = DryerRecipe.recipesFromInputs[item.getItem()]
            if (recipe != null)
            {
                ++time
                if (time > recipe.time)
                {
                    if (world?.rand?.nextBoolean() ?: false)
                    {
                       time = 0
                       item = recipe.result
                       sync()
                    }
                }
            }
            else
            {
                time = 0
            }
        }
    }
}