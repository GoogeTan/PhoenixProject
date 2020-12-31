package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import phoenix.world.StageManager

class SyncStagePacket(var stage : Int, var part : Int) : NetworkHandler.Packet()
{
    constructor() : this(0, 0)
    override fun encode(packet: NetworkHandler.Packet, buf: PacketBuffer)
    {
        if (packet is SyncStagePacket)
        {
            stage = packet.stage
            part = packet.part
            buf.writeInt(stage)
            buf.writeInt(part)
        }
    }

    override fun decode(buf: PacketBuffer) = SyncStagePacket(buf.readInt(), buf.readInt())

    override fun client(player: ClientPlayerEntity?)
    {
        StageManager.setStage(stage)
        StageManager.setPart(part)
    }

    override fun server(player: ServerPlayerEntity?) {}
}