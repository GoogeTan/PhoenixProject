package phoenix.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import phoenix.world.StageManager

class SyncStagePacket(var stage : Int, var part : Int) : NetworkHandler.Packet()
{
    constructor() : this(0, 0)

    override fun client(player: ClientPlayerEntity?)
    {
        StageManager.stage = stage
        StageManager.part = part
    }

    override fun server(player: ServerPlayerEntity?) {}

    object Serializer : NetworkHandler.Packet.Serializer<SyncStagePacket>()
    {
        override fun encode(packet: SyncStagePacket, buf: PacketBuffer) : ByteBuf = buf.writeInt(packet.stage).writeInt(packet.part)
        override fun decode(buf: PacketBuffer) = SyncStagePacket(buf.readInt(), buf.readInt())
    }
}