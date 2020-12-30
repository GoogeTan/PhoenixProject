package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.NonNullList
import net.minecraft.util.math.BlockPos
import phoenix.containers.ash.OvenContainer
import phoenix.tile.ash.OvenTile

class SyncOvenPacket(var timers: IntArray, var burnTime : Int, var container: OvenContainer, var pos: BlockPos) : NetworkHandler.Packet()
{
    override fun encode(packet: NetworkHandler.Packet, buf: PacketBuffer)
    {
        buf.writeVarIntArray(timers)
        buf.writeVarInt(burnTime)
        buf.writeItemStack(container[0])
        buf.writeItemStack(container[1])
        buf.writeItemStack(container[2])
        buf.writeItemStack(container[3])
        buf.writeBlockPos(pos)
    }

    override fun decode(buf: PacketBuffer): NetworkHandler.Packet
    {
        var cont = OvenContainer()
        var timers = buf.readVarIntArray()
        var burnTime = buf.readInt()
        cont[0] = buf.readItemStack()
        cont[1] = buf.readItemStack()
        cont[2] = buf.readItemStack()
        cont[3] = buf.readItemStack()
        var pos = buf.readBlockPos()
        return SyncOvenPacket(timers, burnTime, cont, pos)
    }

    override fun client(player: ClientPlayerEntity?)
    {
        var world = player!!.world
        var tile = world.getTileEntity(pos) as OvenTile
        tile.burnTime = burnTime
        tile.inventory = container
        tile.timers = timers
    }

    override fun server(player: ServerPlayerEntity?)
    {
    }
}