package phoenix.tile.redo.pipe

import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.init.PhxTiles
import phoenix.tile.AFluidTile
import phoenix.utils.*

open class BambooPipeTile
    (
    type : TileEntityType<out BambooPipeTile> = PhxTiles.bambooPipe,
    private val capacity: Int = 1000,
    private val pullAmount: Int = 1000 / 5
    ) : AFluidTile(type), ITickableTileEntity
{
    open var fluidTank : FluidTank = FluidTank(capacity)

    override fun tick()
    {
        val world = world
        if(world != null && !world.isRemote)
        {
            var i = blockState[BlockStateProperties.HORIZONTAL_AXIS].getMainDirection()
            var tile = world.getTileAt<TileEntity>(pos.offset(i))

            if (tile != null)
            {
                val handler = tile.getFluid(i.opposite)
                if (handler.isPresent)
                {
                    val fluid = handler.orElseThrow { NullPointerException("Present fluid tank in not present! It sound like bread, but it is reality.") }
                    needSync = needSync or interact(tile, fluid, i.opposite)
                }
            }

            i =  i.opposite
            tile =  world.getTileAt<TileEntity>(pos.offset(i))
            if(tile != null)
            {
                val handler = tile.getFluid(i.opposite)
                if (handler.isPresent)
                {
                    val fluid = handler.orElseThrow { NullPointerException("Present fluid tank in not present! It sound like bread, but it is reality.") }
                    needSync = needSync or interact(tile, fluid, i.opposite)
                }
            }

            if (needSync)
            {
                sync()
                needSync = false
            }
        }
    }

    override fun interact(tile: TileEntity, fluid: IFluidHandler, side : Direction): Boolean
    {
        val tfluid = this.getFluid(side)
        if (tfluid.isPresent)
            return interactBetweenPipes(tfluid.orElse(null), fluid, pullAmount)
        return false
    }

    override fun sync()
    {
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket = SUpdateTileEntityPacket()

    override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T>
    {
        if (cap === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)// && side?.axis === blockState[BlockStateProperties.HORIZONTAL_AXIS])
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
