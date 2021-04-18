package phoenix.tile.redo

import net.minecraft.fluid.Fluids
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.util.Direction
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.init.PhoenixTiles
import phoenix.network.NetworkHandler
import phoenix.network.SyncFluidThinkPacket
import phoenix.tile.IFluidThing
import phoenix.utils.SerializeUtils.readTank
import phoenix.utils.SerializeUtils.writeToBuf
import phoenix.utils.block.PhoenixTile
import phoenix.utils.getTileAt

class TankTile(capacity : Int) : PhoenixTile<TankTile>(PhoenixTiles.TANK), IFluidThing, ITickableTileEntity
{
    constructor() : this(5)

    override var tank: FluidTank = FluidTank(capacity * 1000)
    override val throughput: Int = 1 * 1000
    override var needSync: Boolean = false

    override fun tick()
    {
        val world = world
        if(world != null && !world.isRemote)
        {
            if(!tank.isEmpty)
            {
                for (i in Direction.values())
                {
                    val tile = world.getTileAt<IFluidThing>(pos.offset(i))
                    if (tile != null)
                    {
                        if ((tile.tank.fluid.fluid == this.tank.fluid.fluid  || tile.tank.fluid.fluid == Fluids.EMPTY) &&  tile.tank.fluidAmount != this.tank.fluidAmount)
                        {
                            if (tile.tank.isEmpty)
                            {
                                this.tank.fluid.amount /= 2
                                tile.tank.fluid = this.tank.fluid
                            }
                            else
                            {
                                val all = tile.tank.fluidAmount + this.tank.fluidAmount
                                tile.tank.fluid.amount = all / 2
                                this.tank.fluid.amount = all / 2
                            }

                            if (tile is TankTile)
                                tile.needSync = true
                            this.needSync = true
                        }
                    }
                }
            }
            if(needSync)
            {
                NetworkHandler.sendToAll(SyncFluidThinkPacket(tank.fluid, pos))
                needSync = false
            }
        }
    }

    override fun read(tag: CompoundNBT)
    {
        tank.readFromNBT(tag)
        super.read(tag)
    }

    override fun write(tagIn: CompoundNBT): CompoundNBT
    {
        tank.writeToNBT(tagIn)
        return super.write(tagIn)
    }

    override fun getUpdateTag(): CompoundNBT
    {
        return write(CompoundNBT())
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket
    {
        return UpdatePacket()
    }

    override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket)
    {
    }

    inner class UpdatePacket : SUpdateTileEntityPacket()
    {
        override fun writePacketData(buf: PacketBuffer)
        {
            super.writePacketData(buf)
            buf.writeToBuf(tank)
        }

        override fun readPacketData(buf: PacketBuffer)
        {
            super.readPacketData(buf)
            tank = buf.readTank()
        }
    }


}