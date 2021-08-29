package phoenix.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import phoenix.client.gui.DiaryGui
import phoenix.other.mc
import phoenix.world.StageManager

class OpenDiaryPacket : Packet()
{
    override fun processClient(player: ClientPlayerEntity?)
    {
        mc?.displayGuiScreen(DiaryGui())
    }

    override fun processServer(player: ServerPlayerEntity?) {}

    object Serializer : Packet.Serializer<OpenDiaryPacket>()
    {
        override fun encode(packet: OpenDiaryPacket, buf: PacketBuffer) : ByteBuf = buf
        override fun decode(buf: PacketBuffer) = OpenDiaryPacket()
    }
}