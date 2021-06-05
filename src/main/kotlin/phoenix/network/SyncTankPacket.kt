package phoenix.network

import net.minecraft.client.entity.player.ClientPlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.tile.redo.JuicerTile
import phoenix.tile.redo.TankTile
import phoenix.utils.SerializeUtils.readTank
import phoenix.utils.SerializeUtils.writeTank
import phoenix.utils.getTileAt
import phoenix.utils.mc

class SyncTankPacket(var pos : BlockPos, var tank : FluidTank, var stack : ItemStack) : NetworkHandler.Packet()
{
    constructor(tankTile : TankTile?) : this(tankTile?.pos?:BlockPos.ZERO, tankTile?.fluidTank?:FluidTank(0), (tankTile as? JuicerTile)?.stack?: ItemStack.EMPTY)
    constructor() : this(null)

    override fun encode(packet: NetworkHandler.Packet, buf: PacketBuffer)
    {
        if(packet is SyncTankPacket)
        {
            pos = packet.pos
            tank = packet.tank
            buf.writeBlockPos(pos)
            buf.writeTank(tank)
            buf.writeItemStack(stack)
        }
    }

    override fun decode(buf: PacketBuffer): NetworkHandler.Packet
    {
        pos = buf.readBlockPos()
        tank = buf.readTank()
        stack = buf.readItemStack()
        return SyncTankPacket(pos, tank, stack)
    }

    override fun client(player: ClientPlayerEntity?)
    {
        val world = mc.world
        if(world != null)
        {
            val tile = world.getTileAt<TankTile>(pos)
            if(tile != null)
            {
                tile.fluidTank = tank
                if(tile is JuicerTile)
                    tile.stack = stack
            }
        }
    }

    override fun server(player: ServerPlayerEntity?) {}
}