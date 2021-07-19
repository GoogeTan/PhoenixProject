package phoenix.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import phoenix.api.entity.Date
import phoenix.api.entity.IPhoenixPlayer
import phoenix.other.readDate
import phoenix.other.writeDate

class SyncBookPacket(var list : List<Pair<Int, Date>>): Packet()
{
    override fun processClient(player: ClientPlayerEntity?)
    {
        if(player is IPhoenixPlayer)
        {
            for (i in list)
                player.addChapter(i.first, i.second)
        }
    }

    override fun processServer(player: ServerPlayerEntity?) {}

    object Serializer :     Packet.Serializer<SyncBookPacket>()
    {
        override fun encode(packet: SyncBookPacket, buf: PacketBuffer) : ByteBuf
        {
            for (i in packet.list)
            {
                buf.writeInt(i.first)
                buf.writeDate(i.second)
            }
            return buf
        }

        override fun decode(buf: PacketBuffer): SyncBookPacket
        {
            val res = ArrayList<Pair<Int, Date>>()

            val count = buf.readInt()
            for (i in 0 until count)
            {
                val id = buf.readInt()
                val date = buf.readDate()
                res.add(Pair(id, date))
            }

            return SyncBookPacket(res)
        }
    }
}