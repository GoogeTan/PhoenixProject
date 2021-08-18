package phoenix.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import phoenix.other.getTileAt
import phoenix.tile.ash.DryerTile

class SyncDryerPacket(var pos : BlockPos, var stack : ItemStack): Packet()
{
    constructor() : this(BlockPos.ZERO, ItemStack.EMPTY)

    override fun processClient(player: ClientPlayerEntity?)
    {
        super.processClient(player)
        player?.world?.getTileAt<DryerTile>(pos)?.item = stack
    }

    object Serializer : Packet.Serializer<SyncDryerPacket>()
    {
        override fun encode(packet: SyncDryerPacket, buf: PacketBuffer): ByteBuf = buf.writeBlockPos(packet.pos).writeItemStack(packet.stack)

        override fun decode(buf: PacketBuffer): SyncDryerPacket = SyncDryerPacket(buf.readBlockPos(), buf.readItemStack())
    }
}