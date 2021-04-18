package phoenix.tile.redo

import net.minecraft.fluid.Fluids
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.util.Direction
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.init.PhoenixTiles.OVEN
import phoenix.init.PhoenixTiles.PIPE
import phoenix.tile.IFluidThing
import phoenix.tile.ash.OvenTile
import phoenix.utils.LogManager
import phoenix.utils.block.PhoenixTile
import phoenix.utils.getTileAt

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
        }
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket = UpdatePacket()

    override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket) { }

    inner class UpdatePacket : SUpdateTileEntityPacket()
}
