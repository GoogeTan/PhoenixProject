package phoenix.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import phoenix.tile.ash.OvenTile
import phoenix.utils.getTileAt

class SyncOvenPacket(var timers: IntArray, var burnTime : Int, var container: IInventory, var pos: BlockPos) : Packet()
{
    constructor(tile: OvenTile) : this(tile.timers, tile.burnTime, tile, tile.pos)

    override fun processClient(player: ClientPlayerEntity?)
    {
        val world = player!!.world
        val tile = world.getTileAt<OvenTile>(pos)
        if(tile != null)
        {
            tile.burnTime = burnTime
            for (i in 0..3)
                tile.setInventorySlotContents(i, container.getStackInSlot(i))
            tile.timers = timers
        }
    }

    object Serializer : Packet.Serializer<SyncOvenPacket>()
    {
        override fun encode(packet: SyncOvenPacket, buf: PacketBuffer) : ByteBuf
        {
            for (i in 0..3)
                buf.writeItemStack(packet.container.getStackInSlot(i))
            buf.writeBlockPos(packet.pos)
            buf.writeVarIntArray(packet.timers)
            buf.writeVarInt(packet.burnTime)
            return buf
        }

        override fun decode(buf: PacketBuffer): SyncOvenPacket
        {
            val cont = Inventory(4)
            for (i in 0..3)
                cont.setInventorySlotContents(i, buf.readItemStack())
            val pos = buf.readBlockPos()
            val timers = buf.readVarIntArray()
            val burnTime = buf.readVarInt()
            return SyncOvenPacket(timers, burnTime, cont, pos)
        }
    }
}