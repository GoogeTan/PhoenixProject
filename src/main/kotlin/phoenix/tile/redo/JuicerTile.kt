package phoenix.tile.redo

import net.minecraft.client.network.play.IClientPlayNetHandler
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntityType
import phoenix.init.PhxTiles
import phoenix.tile.AFluidTankTile

class JuicerTile(type : TileEntityType<out JuicerTile> = PhxTiles.JUICER) : AFluidTankTile(type, 2)
{
    override var needSync: Boolean = false



    override fun getUpdatePacket() = this.SyncPacket()

    inner class SyncPacket : FluidTileUpdatePacket()
    {
        override fun readPacketData(buffer : PacketBuffer)
        {
            super.readPacketData(buffer)
        }

        override fun writePacketData(buffer: PacketBuffer)
        {
            super.writePacketData(buffer)
        }

        override fun processPacket(handler : IClientPlayNetHandler)
        {
            super.processPacket(handler)
        }
    }
}