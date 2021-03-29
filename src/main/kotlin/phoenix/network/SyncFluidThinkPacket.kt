package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fluids.FluidStack
import phoenix.tile.IFluidThing
import phoenix.utils.clientPlayer
import phoenix.utils.getTileAt
import phoenix.utils.mc
import phoenix.utils.sendMessage

class SyncFluidThinkPacket(var fluid: FluidStack, var pos: BlockPos) : NetworkHandler.Packet()
{
    constructor() : this(FluidStack.EMPTY, BlockPos.ZERO)

    override fun encode(packet: NetworkHandler.Packet, buf: PacketBuffer)
    {
        if(packet is SyncFluidThinkPacket)
        {
            fluid = packet.fluid
            pos = packet.pos
            buf.writeFluidStack(fluid)
            buf.writeBlockPos(pos)
        }
    }

    override fun decode(buf: PacketBuffer): NetworkHandler.Packet = SyncFluidThinkPacket(buf.readFluidStack(), buf.readBlockPos())

    override fun client(player: ClientPlayerEntity?)
    {
        val world = mc.world
        if(world != null)
        {
            val tile = world.getTileAt<IFluidThing>(pos)
            if(tile != null)
            {
                tile.tank.fluid = fluid
                clientPlayer?.sendMessage("tile at $pos synced")
            }
            else
                clientPlayer?.sendMessage("tile at $pos is null")
        }
        else
            clientPlayer?.sendMessage("world is null")
    }

    override fun server(player: ServerPlayerEntity?) {}
}