package phoenix.tile.redo

import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.util.Direction
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.init.PhoenixTiles.PIPE
import phoenix.tile.IFluidThing
import phoenix.utils.block.PhoenixTile
import phoenix.utils.getTileAt
import kotlin.math.abs

class PipeTile(maxCapacity : Int) : PhoenixTile<PipeTile>(PIPE), ITickableTileEntity, IFluidThing
{
    override var tank = FluidTank(maxCapacity * 1000)
    override val throughput: Int = 1 * 1000
    override var needSync: Boolean = false

    constructor() : this(1)

    override fun read(tag: CompoundNBT)
    {
        super.read(tag)
        tank.readFromNBT(tag.getCompound("tank"))
    }

    override fun write(tagIn: CompoundNBT): CompoundNBT
    {
        val tag = super.write(tagIn)
        tag.put("tank", tank.writeToNBT(CompoundNBT()))
        return tag
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket = UpdatePacket()

    override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket)
    {
    }

    override fun tick()
    {
        val world = world
        if(world != null)
        {
            if(!tank.isEmpty)
            {
                for (i in Direction.values())
                {
                    val tile = world.getTileAt<IFluidThing>(pos.offset(i))
                    if (tile != null)
                    {
                        if(tile.tank.isEmpty)
                            tile.tank.fluid = FluidStack(tank.fluid.fluid, 0)

                        if (tile.tank.fluid.fluid == this.tank.fluid.fluid && abs(tile.getPercent() - this.getPercent()) > 0.001)
                        {
                            val all = tile.tank.fluidAmount + this.tank.fluidAmount
                            tile.tank.fluid.amount = tile.tank.capacity * all / (tile.tank.capacity + this.tank.capacity)
                            this.tank.fluid.amount = all - tile.tank.fluid.amount
                            if(tile is TankTile)
                            {
                                tile.needSync = true
                            }
                        }
                    }
                }
            }
        }
    }

    inner class UpdatePacket : SUpdateTileEntityPacket()
    {
        override fun readPacketData(buf: PacketBuffer)
        {
            super.readPacketData(buf)
            //buf.writeFluidStack(tank.fluid)
        }

        override fun writePacketData(buf: PacketBuffer)
        {
            super.writePacketData(buf)
            //tank.fluid = buf.readFluidStack()
        }
    }
}
