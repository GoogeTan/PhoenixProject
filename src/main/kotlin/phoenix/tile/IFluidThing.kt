package phoenix.tile

import net.minecraftforge.fluids.capability.templates.FluidTank

interface IFluidThing
{
    var tank : FluidTank
    val throughput : Int
    var needSync : Boolean

    fun getPercent() : Double = tank.fluidAmount.toDouble() / tank.capacity.toDouble()
}