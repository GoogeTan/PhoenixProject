package phoenix.tile

import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.state.IProperty
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.api.tile.ISyncable
import phoenix.api.tile.PhoenixTile
import phoenix.other.*

abstract class FluidTileSidable
    (
        type: TileEntityType<out FluidTileSidable>,
        protected val facing : IProperty<Direction>,
        protected val pullAmount : Int = 1000,
        protected var capacity : Int = 1000
    ) : PhoenixTile(type), ITickableTileEntity, ISyncable
{
    override var needSync: Boolean = false
    open var fluidTank: FluidTank = FluidTank(capacity)

    override fun tick()
    {
        val world = world
        if (world != null && !world.isRemote)
        {
            val (input, output) = getDirections()
            if (input != null)
            {
                val tile = world.getTileAt<TileEntity>(pos.offset(input))
                if (tile != null)
                {
                    val handler = tile.getFluid(input.opposite)
                    if (handler.isPresent)
                    {
                        val fluid =
                            handler.orElseThrow { NullPointerException("Present fluid tank in not present! It sound like bread, but it is reality.") }
                        needSync = needSync or fill(tile, fluid, input.opposite)
                    }
                }
            }
            if (output != null)
            {
                val tile = world.getTileAt<TileEntity>(pos.offset(output))
                if (tile != null)
                {
                    val handler = tile.getFluid(output.opposite)
                    if (handler.isPresent)
                    {
                        val fluid =
                            handler.orElseThrow { NullPointerException("Present fluid tank in not present! It sound like bread, but it is reality.") }
                        needSync = needSync or extract(tile, fluid, output.opposite)
                    }
                }
            }
            if (true)
            {
                sync()
                needSync = false
            }
        }
    }

    // first - input, second - output
    open fun getDirections(): MutablePair<Direction?, Direction?> = uniquePairOf(blockState[facing], blockState[facing].opposite)

    open fun extract(tile: TileEntity, fluid: IFluidHandler, side: Direction): Boolean
    {
        val tank = this.getFluid(side)
        var res = false
        tank.ifPresent { value ->
            res = extract(value, fluid, pullAmount)
        }
        return res
    }

    open fun fill(tile: TileEntity, fluid: IFluidHandler, side: Direction): Boolean
    {
        val tfluid = this.getFluid(side)
        var res = false
        tfluid.ifPresent { value ->
            res = fill(value, fluid, pullAmount)
        }
        return res
    }

    open fun extract(
        tank: IFluidHandler,
        other: IFluidHandler,
        max: Int,
        tankIndex: Int = 0,
        otherIndex: Int = 0
    ): Boolean
    {
        val ff = tank.getFluidInTank(tankIndex)
        val sf = other.getFluidInTank(otherIndex)
        return if (areFluidsCompatible(sf, ff))
        {
            val amount = min(
                max,
                other.getTankCapacity(otherIndex) - other.getFluidInTank(otherIndex).amount,
                tank.getFluidInTank(tankIndex).amount
            )
            val res =
                other.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE)
            tank.drain(res, IFluidHandler.FluidAction.EXECUTE)
            res != 0
        } else false
    }

    open fun fill(tank: IFluidHandler, other: IFluidHandler, max: Int, tankIndex: Int = 0, otherIndex: Int = 0): Boolean
    {
        val ff = tank.getFluidInTank(tankIndex)
        val sf = other.getFluidInTank(otherIndex)
        return if (areFluidsCompatible(sf, ff))
        {
            val amount = min(
                max,
                tank.getTankCapacity(tankIndex) - ff.amount,
                tank.getTankCapacity(otherIndex) - tank.getFluidInTank(otherIndex).amount,
                other.getFluidInTank(tankIndex).amount
            )
            val res =
                other.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE)
            tank.drain(res, IFluidHandler.FluidAction.EXECUTE)
            res != 0
        } else false
    }

    open fun sync()
    {

    }

    override fun readPacketData(buf: PacketBuffer)
    {
        super.readPacketData(buf)
        fluidTank = buf.readFluidTank()
        capacity = buf.readInt()
    }

    override fun writePacketData(buf: PacketBuffer)
    {
        super.writePacketData(buf)
        buf.writeFluidTank(fluidTank)
        buf.writeInt(capacity)
    }

    override fun getUpdateTag(): CompoundNBT = write(super.getUpdateTag())

    override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T>
    {
        if (cap === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side in getDirections())
            return LazyOptional.of { (fluidTank as T) }
        return super.getCapability(cap, side)
    }
}
