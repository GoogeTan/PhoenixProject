package phoenix.tile.redo

import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.init.PhxTiles
import phoenix.network.NetworkHandler
import phoenix.network.SyncTankPacket
import phoenix.tile.AFluidTile
import phoenix.tile.ISyncable
import phoenix.utils.*
import phoenix.utils.SerializeUtils.readTank
import phoenix.utils.SerializeUtils.writeTank
import phoenix.utils.SizedArrayList.Companion.of

open class TankTile
    (
        type : TileEntityType<out TankTile> = PhxTiles.tank,
        var capacity : Int = 5000,
        val pullAmount : Int = 1000
    ) : AFluidTile(type), ITickableTileEntity
{
    override var needSync: Boolean = true
        set(value) { field = true }

    open var fluidTank : FluidTank = FluidTank(capacity)

    override fun tick()
    {
        val world = world
        if (world != null && !world.isRemote)
        {
            var i = getInputDirection()
            var tile = world.getTileAt<TileEntity>(pos.offset(i))

            if (tile != null)
            {
                val handler = tile.getFluid(i.opposite)
                if (handler.isPresent)
                {
                    val fluid = handler.orElseThrow { NullPointerException("Present fluid tank in not present! It sound like bread, but it is reality.") }
                    val res = fill(tile, fluid, i.opposite)
                    needSync = needSync or res
                    if (tile is ISyncable)
                        tile.needSync = tile.needSync or res
                }
            }

            i = i.opposite
            tile = world.getTileAt<TileEntity>(pos.offset(i))
            if (tile != null)
            {
                val handler = tile.getFluid(i.opposite)
                if (handler.isPresent)
                {
                    val fluid = handler.orElseThrow { NullPointerException("Present fluid tank in not present! It sound like bread, but it is reality.") }
                    val res = extract(tile, fluid, i.opposite)
                    needSync = needSync or res
                    if (tile is ISyncable)
                        tile.needSync = tile.needSync or res
                }
            }

            if (needSync)
            {
                sync()
                needSync = false
            }
        }
    }

    fun getInputDirection() = blockState[BlockStateProperties.FACING]

    override fun interact(tile: TileEntity, fluid: IFluidHandler, side : Direction): Boolean { return false }

    override fun sync()
    {
        NetworkHandler.sendToDim(SyncTankPacket(this), world!!.dimension.type)
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket = this.SyncPacket()

    open inner class SyncPacket : SUpdateTileEntityPacket()
    {
        override fun readPacketData(buffer: PacketBuffer)
        {
            super.readPacketData(buffer)
            fluidTank = buffer.readTank()
            capacity = buffer.readInt()
        }

        override fun writePacketData(buffer: PacketBuffer)
        {
            super.writePacketData(buffer)
            buffer.writeTank(fluidTank)
            buffer.writeInt(capacity)
        }
    }

    override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T>
    {
        if (cap === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (side === getInputDirection() || side === getInputDirection().opposite))
            return LazyOptional.of { fluidTank as T }
        return super.getCapability(cap, side)
    }

    private fun extract(tile: TileEntity, fluid: IFluidHandler, side : Direction): Boolean
    {
        val tank = this.getFluid(side)
        return if (tank.isPresent) extract(tank.orElse(null), fluid, pullAmount) else false
    }

    private fun extract(tank: IFluidHandler, other: IFluidHandler, max: Int, tankIndex : Int = 0, otherIndex : Int = 0) : Boolean
    {
        val ff = tank.getFluidInTank(tankIndex)
        val sf = other.getFluidInTank(otherIndex)
        return if (areFluidsCompatible(sf, ff))
        {
            val amount = min(max, other.getTankCapacity(otherIndex) - other.getFluidInTank(otherIndex).amount, tank.getFluidInTank(tankIndex).amount)
            val res = other.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE)
            tank.drain(res, IFluidHandler.FluidAction.EXECUTE)
            res != 0
        } else false
    }

    private fun fill(tile: TileEntity, fluid: IFluidHandler, side : Direction): Boolean
    {
        val tfluid = this.getFluid(side)
        if (tfluid.isPresent)
            return fill(tfluid.orElse(null), fluid, pullAmount)
        return false
    }

    private fun fill(tank: IFluidHandler, other: IFluidHandler, max: Int, tankIndex : Int = 0, otherIndex : Int = 0) : Boolean
    {
        val ff = tank.getFluidInTank(tankIndex)
        val sf = other.getFluidInTank(otherIndex)
        return if (areFluidsCompatible(sf, ff))
        {
            val amount = min(max, tank.getTankCapacity(tankIndex) - ff.amount, tank.getTankCapacity(otherIndex) - tank.getFluidInTank(otherIndex).amount, other.getFluidInTank(tankIndex).amount)
            val res = other.fill(tank.drain(amount, IFluidHandler.FluidAction.SIMULATE), IFluidHandler.FluidAction.EXECUTE)
            tank.drain(res, IFluidHandler.FluidAction.EXECUTE)
            res != 0
        } else false
    }
}