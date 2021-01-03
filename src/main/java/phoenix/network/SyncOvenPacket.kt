package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import phoenix.blocks.ash.OvenBlock
import phoenix.containers.ash.OvenContainer
import phoenix.tile.ash.OvenTile

class SyncOvenPacket(var timers: IntArray, var burnTime : Int, var container: OvenContainer, var pos: BlockPos) : NetworkHandler.Packet()
{
    constructor() : this(IntArray(4), 0, OvenContainer(), BlockPos.ZERO.add(0, 5, 0))
    constructor(tile: OvenTile) : this(tile.timers, tile.burnTime, tile.inventory, tile.pos)

    override fun encode(packet: NetworkHandler.Packet, buf: PacketBuffer)
    {
        if(packet is SyncOvenPacket)
        {
            pos = packet.pos
            container = packet.container
            burnTime = packet.burnTime
            timers = packet.timers
            buf.writeItemStack(packet.container[0])
            buf.writeItemStack(packet.container[1])
            buf.writeItemStack(packet.container[2])
            buf.writeItemStack(packet.container[3])
            buf.writeBlockPos(packet.pos)
            buf.writeVarIntArray(packet.timers)
            buf.writeVarInt(packet.burnTime)
        }
    }

    override fun decode(buf: PacketBuffer): NetworkHandler.Packet
    {
        val cont = OvenContainer()
        cont[0] = buf.readItemStack()
        cont[1] = buf.readItemStack()
        cont[2] = buf.readItemStack()
        cont[3] = buf.readItemStack()
        val pos      = buf.readBlockPos()
        val timers   = buf.readVarIntArray()
        val burnTime = buf.readVarInt()
        return SyncOvenPacket(timers, burnTime, cont, pos)
    }

    override fun client(player: ClientPlayerEntity?)
    {

        val world = player!!.world
        val tile = world.getTileEntity(pos) as OvenTile?
        if(tile != null)
        {
            tile.burnTime = burnTime
            tile.inventory = container
            tile.timers = timers
        }
    }

    override fun server(player: ServerPlayerEntity?) {}

    override fun toString() = "SyncOvenPacket(timers=${timers.contentToString()}, burnTime=$burnTime, container=$container, pos=$pos)"
}