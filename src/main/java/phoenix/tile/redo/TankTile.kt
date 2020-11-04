package phoenix.tile.redo

import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.init.PhoenixTiles
import phoenix.utils.IFluidMechanism

class TankTile : TileEntity(PhoenixTiles.TANK.get()), IFluidMechanism
{
    private var numberInGraph = 0
    public var tank = FluidTank(FluidAttributes.BUCKET_VOLUME * 5)
    private val holder = LazyOptional.of<IFluidHandler> { tank }

    override fun read(tag: CompoundNBT)
    {
        super.read(tag)
        tank.readFromNBT(tag)
        numberInGraph = tag.getInt("number_in_graph")
    }

    override fun write(tagIn: CompoundNBT): CompoundNBT
    {
        var tag = tagIn
        tag = super.write(tag)
        tank.writeToNBT(tag)
        tag.putInt("number_in_graph", numberInGraph)
        return tag
    }

    override fun <T> getCapability(capability: Capability<T>, facing: Direction?): LazyOptional<T>
    {
        return if (capability === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) holder.cast() else super.getCapability(capability, facing)
    }

    override fun getNumberInGraph(): Int = numberInGraph
    override fun setNumberInGraph(number_in_graph: Int) { numberInGraph = number_in_graph }

    override fun getInput (): FluidTank  = tank
    override fun getOutput(): FluidTank  = tank
    override fun isEndOrStart(): Boolean = true
}