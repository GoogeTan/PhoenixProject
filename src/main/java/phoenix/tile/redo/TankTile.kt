package phoenix.tile.redo

import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.Direction
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.init.PhoenixTiles
import phoenix.utils.GraphNode
import phoenix.utils.pipe.FluidGraphSaveData
import phoenix.utils.pipe.IFluidMechanism
import java.util.*

class TankTile : TileEntity(PhoenixTiles.TANK.get()), IFluidMechanism, ITickableTileEntity
{
    private var stack = ItemStack.EMPTY;
    private var numberInGraph = 0
    var tank = FluidTank(FluidAttributes.BUCKET_VOLUME * 5)
    private val holder = LazyOptional.of<IFluidHandler> { tank }
    private var inputs  : ArrayList<GraphNode>? = null
    private var outputs : ArrayList<GraphNode>? = null

    override fun tick()
    {
        if(!(world!!.isRemote()) && world != null)
        {
            try
            {
                if (inputs == null || outputs == null)
                {
                    val res = FluidGraphSaveData.get(world as ServerWorld?).findConnectedMechanisms(numberInGraph)
                    inputs = res.first
                    outputs = res.second
                }
                for (current in outputs!!)
                {
                    val tile: TileEntity? = world?.getTileEntity(current.pos);
                    if (tile is IFluidMechanism)
                    {
                        val mechanism: IFluidMechanism = tile;
                        val currentTank = mechanism.input;
                        if (currentTank.fluidAmount < currentTank.capacity && currentTank.isFluidValid(tank.fluid) && currentTank.fluidAmount < tank.fluidAmount)
                        {
                            currentTank.fill(tank.fluid, IFluidHandler.FluidAction.EXECUTE)
                        }
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
    }

    override fun toString(): String
    {
        var s = "TankTile{ stack= ";
        s += stack;
        s += " number in graph = "
        s += numberInGraph
        s += " tank = "
        s += tank
        return s
    }

    override fun read(tag: CompoundNBT)
    {
        super.read(tag)
        tank.readFromNBT(tag)
        stack = ItemStack.read(tag)
        numberInGraph = tag.getInt("number_in_graph")
    }

    override fun write(tagIn: CompoundNBT): CompoundNBT
    {
        val tag = super.write(tagIn)

        tank.writeToNBT(tag)
        stack.write(tag)
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