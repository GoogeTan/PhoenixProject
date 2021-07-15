package phoenix.network

import io.netty.buffer.ByteBuf
import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.tile.redo.TankTile
import phoenix.utils.getTileAt
import phoenix.utils.mc
import phoenix.utils.readFluidTank
import phoenix.utils.writeFluidTank

class SyncTankPacket(var pos : BlockPos, var tank : FluidTank) : Packet()
{
    override fun processClient(player: ClientPlayerEntity?)
    {
        val world = mc.world
        if(world != null)
        {
            val tile = world.getTileAt<TankTile>(pos) ?: return
            tile.fluidTank = tank
        }
    }

    object Serializer : Packet.Serializer<SyncTankPacket>()
    {
        override fun encode(packet: SyncTankPacket, buf: PacketBuffer) : ByteBuf = buf.writeBlockPos(packet.pos).writeFluidTank(packet.tank)
        override fun decode(buf: PacketBuffer): SyncTankPacket                   = SyncTankPacket(buf.readBlockPos(), buf.readFluidTank())
    }
}