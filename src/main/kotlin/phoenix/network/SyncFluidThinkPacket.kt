package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.tile.AFluidTile
import phoenix.utils.*
import phoenix.utils.SerializeUtils.readTank
import phoenix.utils.SerializeUtils.writeToBuf

class SyncFluidThinkPacket(var inputs: SizedArrayList<FluidTank>, var outputs: SizedArrayList<FluidTank>, var pos: BlockPos) : NetworkHandler.Packet()
{
    constructor() : this(SizedArrayList(), SizedArrayList(), BlockPos.ZERO)
    constructor(tile : AFluidTile) : this(tile.inputs, tile.outputs, tile.pos)
    override fun encode(packet: NetworkHandler.Packet, buf: PacketBuffer)
    {
        if(packet is SyncFluidThinkPacket)
        {
            inputs = packet.inputs
            outputs = packet.outputs
            pos = packet.pos
            buf.writeBlockPos(pos)
            buf.writeInt(inputs.size)
            for (i in inputs)
                buf.writeToBuf(i)
            buf.writeInt(outputs.size)
            for (i in outputs)
                buf.writeToBuf(i)
        }
    }

    override fun decode(buf: PacketBuffer): NetworkHandler.Packet
    {
        val inputs = SizedArrayList<FluidTank>()
        val outputs = SizedArrayList<FluidTank>()
        val pos = buf.readBlockPos()
        for (i in 0 until buf.readInt())
            inputs.add(buf.readTank())
        for (i in 0 until buf.readInt())
            outputs.add(buf.readTank())

        return SyncFluidThinkPacket(inputs, outputs, pos)
    }

    override fun client(player: ClientPlayerEntity?)
    {
        val world = mc.world
        if(world != null)
        {
            val tile = world.getTileAt<AFluidTile>(pos)
            if(tile != null)
            {
                tile.inputs = inputs
                tile.outputs = outputs
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