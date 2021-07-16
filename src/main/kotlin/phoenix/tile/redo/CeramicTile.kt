package phoenix.tile.redo

import net.minecraft.block.material.MaterialColor
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.TileEntityType
import phoenix.init.PhxTiles
import phoenix.utils.block.PhoenixTile

class CeramicTile
    (
    tileEntityTypeIn: TileEntityType<out CeramicTile> = PhxTiles.ceramic,
    var colour: Int = MaterialColor.YELLOW_TERRACOTTA.colorValue,
    var hardness: Float = 3.0f,
    var resistance: Float = 3.0f
    ) : PhoenixTile(tileEntityTypeIn)
{
    override fun serializeNBT(): CompoundNBT
    {
        val res = super.serializeNBT()
        res.putInt("colour", colour)
        res.putFloat("hardness", hardness)
        res.putFloat("resistance", resistance)
        return res
    }

    override fun deserializeNBT(tag: CompoundNBT)
    {
        colour = if (tag.contains("colour")) tag.getInt("colour") else MaterialColor.BLUE_TERRACOTTA.colorValue
        hardness = tag.getFloat("hardness")
        resistance = tag.getFloat("resistance")
        if (!tag.contains("empty"))
            super.deserializeNBT(tag)
    }

    override fun readPacketData(buf: PacketBuffer)
    {
        super.readPacketData(buf)
        colour = buf.readInt()
        hardness = buf.readFloat()
        resistance = buf.readFloat()
    }

    override fun writePacketData(buf: PacketBuffer)
    {
        super.writePacketData(buf)
        buf.writeInt(colour)
        buf.writeFloat(hardness)
        buf.writeFloat(resistance)
    }
}
