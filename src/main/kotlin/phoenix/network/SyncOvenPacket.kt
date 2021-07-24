package phoenix.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import phoenix.blocks.ash.OvenData
import phoenix.other.getTileAt
import phoenix.tile.ash.OvenTile

class SyncOvenPacket(val data : OvenData, var pos: BlockPos) : Packet()
{
    constructor(tile: OvenTile) : this(tile.data, tile.pos)

    override fun processClient(player: ClientPlayerEntity?)
    {
        val world = player!!.world
        val tile = world.getTileAt<OvenTile>(pos)
        if(tile != null)
        {
            tile.data replace data
        }
    }

    object Serializer : Packet.Serializer<SyncOvenPacket>()
    {
        override fun encode(packet: SyncOvenPacket, buf: PacketBuffer) : ByteBuf
        {
            buf.writeBlockPos(packet.pos)
            packet.data.writeToBuf(buf)
            return buf
        }

        override fun decode(buf: PacketBuffer): SyncOvenPacket
        {
            val pos = buf.readBlockPos()
            val data = OvenData(0)
            data.readFromBuf(buf)
            return SyncOvenPacket(data, pos)
        }
    }
}