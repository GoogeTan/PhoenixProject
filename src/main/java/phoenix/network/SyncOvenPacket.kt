package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos

class SyncOvenPacket(var list : NonNullList<ItemStack>, var pos: BlockPos) : NetworkHandler.Packet()
{
    override fun encode(packet: NetworkHandler.Packet, buf: PacketBuffer)
    {
        buf.writeBlockPos(pos)
        buf.writeInt(list.size)
        for(i in 0 until list.size)
            buf.writeItemStack(list[i])
    }

    override fun decode(buf: PacketBuffer): NetworkHandler.Packet
    {
        val pos = buf.readBlockPos()
        val lists = NonNullList.create<ItemStack>()
        val i = buf.readInt();
        for(i in 0 until i)
            lists.add(buf.readItemStack())
        return SyncOvenPacket(lists, pos)
    }

    override fun client(player: ClientPlayerEntity?)
    {
    }

    override fun server(player: ServerPlayerEntity?)
    {
    }
}