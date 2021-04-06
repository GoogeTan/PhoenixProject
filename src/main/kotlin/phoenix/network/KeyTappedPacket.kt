package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer

class KeyTappedPacket(var key : Int) : NetworkHandler.Packet()
{
    override fun encode(packet: NetworkHandler.Packet, buf: PacketBuffer)
    {
        if(packet is KeyTappedPacket)
        {
            key = packet.key
            buf.writeInt(key)
        }
    }

    override fun decode(buf: PacketBuffer): NetworkHandler.Packet = OpenCaudaInventoryPacket(buf.readInt())

    override fun client(player: ClientPlayerEntity?)
    {
        TODO("Not yet implemented")
    }

    override fun server(player: ServerPlayerEntity?)
    {
        TODO("Not yet implemented")
    }
}