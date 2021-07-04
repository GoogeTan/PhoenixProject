package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.Inventory
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import phoenix.tile.ash.OvenTile
import phoenix.utils.getTileAt

class SyncOvenPacket(var timers: IntArray, var burnTime : Int, var container: IInventory, var pos: BlockPos) : NetworkHandler.Packet()
{
    constructor() : this(IntArray(4), 0, OvenTile(), BlockPos.ZERO.add(0, 5, 0))
    constructor(tile: OvenTile) : this(tile.timers, tile.burnTime, tile, tile.pos)

    override fun encode(packet: NetworkHandler.Packet, buf: PacketBuffer)
    {
        if(packet is SyncOvenPacket)
        {
            pos = packet.pos
            container = packet.container
            burnTime = packet.burnTime
            timers = packet.timers
            buf.writeItemStack(packet.container.getStackInSlot(0))
            buf.writeItemStack(packet.container.getStackInSlot(1))
            buf.writeItemStack(packet.container.getStackInSlot(2))
            buf.writeItemStack(packet.container.getStackInSlot(3))
            buf.writeBlockPos(packet.pos)
            buf.writeVarIntArray(packet.timers)
            buf.writeVarInt(packet.burnTime)
        }
    }

    override fun decode(buf: PacketBuffer): NetworkHandler.Packet
    {
        val cont = Inventory(4)
        cont.setInventorySlotContents(0, buf.readItemStack())
        cont.setInventorySlotContents(1, buf.readItemStack())
        cont.setInventorySlotContents(2, buf.readItemStack())
        cont.setInventorySlotContents(3, buf.readItemStack())
        val pos      = buf.readBlockPos()
        val timers   = buf.readVarIntArray()
        val burnTime = buf.readVarInt()
        return SyncOvenPacket(timers, burnTime, cont, pos)
    }

    override fun client(player: ClientPlayerEntity?)
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

    override fun server(player: ServerPlayerEntity?) {}

    override fun toString() = "SyncOvenPacket(timers=${timers.contentToString()}, burnTime=$burnTime, container=$container, pos=$pos)"

}