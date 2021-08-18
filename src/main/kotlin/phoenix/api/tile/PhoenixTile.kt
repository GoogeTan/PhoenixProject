package phoenix.api.tile

import io.netty.buffer.ByteBuf
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import phoenix.network.Packet

abstract class PhoenixTile(tileEntityTypeIn: TileEntityType<out PhoenixTile>) : TileEntity(tileEntityTypeIn)
{
    override fun getUpdateTag(): CompoundNBT = write(CompoundNBT())

    final override fun getUpdatePacket(): SUpdateTileEntityPacket = UpdatePacket()
    final override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket) { super.onDataPacket(net, pkt) }

    open fun readPacketData(buf: PacketBuffer) {}

    open fun writePacketData(buf: PacketBuffer) {}


    inner class UpdatePacket: SUpdateTileEntityPacket()
    {
        override fun readPacketData(buf: PacketBuffer)
        {
            super.readPacketData(buf)
            this@PhoenixTile.readPacketData(buf)
        }

        override fun writePacketData(buf: PacketBuffer)
        {
            super.writePacketData(buf)
            this@PhoenixTile.writePacketData(buf)
        }
    }
}


abstract class TickablePhoenixTile(tileEntityTypeIn: TileEntityType<out PhoenixTile>) : PhoenixTile(tileEntityTypeIn), ITickableTileEntity
{
    final override fun tick()
    {
        universalTick()
        if (world!!.isRemote)
            clientTick()
        else
            serverTick()
    }

    open fun universalTick()
    {

    }

    open fun clientTick()
    {

    }

    open fun serverTick()
    {

    }
}