package phoenix.tile.redo

import com.mojang.datafixers.util.Pair
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.util.Direction
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.init.PhoenixTiles
import phoenix.utils.SerializeUtils
import phoenix.utils.block.PhoenixTile
import phoenix.utils.graph.GraphNode
import phoenix.utils.graph.MGraphNode
import phoenix.utils.pipe.FluidGraphSaveData
import phoenix.utils.pipe.IFluidMechanism
import java.util.*
import kotlin.math.min

class TankTile : PhoenixTile<TankTile>(PhoenixTiles.TANK), IFluidMechanism, ITickableTileEntity
{
    private var stack = ItemStack.EMPTY
    private var numberInGraph = 0
    var tank = FluidTank(FluidAttributes.BUCKET_VOLUME * 5)
    private val holder = LazyOptional.of<IFluidHandler> { tank }
    private var inputs  : ArrayList<MGraphNode>? = null
    private var outputs : ArrayList<MGraphNode>? = null
    private var colorTmp = ArrayList<Integer>()

    override fun tick()
    {
    }

    override fun getConnectedMechanisms(numberInGraph: Int): Pair<ArrayList<GraphNode>, ArrayList<GraphNode>>
    {
        if(world is ServerWorld)
        {
            if(outputs == null || inputs == null)
            {
                outputs = ArrayList()
                inputs = ArrayList()
                colorTmp = ArrayList(FluidGraphSaveData.get(world as ServerWorld?).graph.size)
                findMechanisms(numberInGraph, Integer(0), 25, ArrayList())
            }
        }
        return Pair.of(inputs?.clone() as ArrayList<GraphNode>, outputs?.clone() as ArrayList<GraphNode>)
    }

    /*
     * It is simply BFS
     */
    private fun findMechanisms(v: Int, color: Integer, maxColor: Int, path: ArrayList<Int>)
    {
        val data = FluidGraphSaveData.get(world as ServerWorld?);
        path.add(v)
        colorTmp[v] = color
        if (data.elements[v].isOutput) outputs?.add(MGraphNode(data.elements[v].world, data.elements[v].pos, color.toInt(), path, data.elements[v].isInput, data.elements[v].isOutput))
        if (data.elements[v].isInput)   inputs?.add(MGraphNode(data.elements[v].world, data.elements[v].pos, color.toInt(), path, data.elements[v].isInput, data.elements[v].isOutput))
        if (color < maxColor)
            for (i in data.graph[v])
                if (colorTmp[i].toInt() != 0) findMechanisms(i, Integer(color.toInt() + 1), maxColor, path)
    }

    override fun removeMechanismByIndex(index: Int)
    {
        TODO("Not yet implemented")
    }

    override fun addMechanismByIndex(world : ServerWorld, index: Int)
    {

        var neighbors = FluidGraphSaveData.get(world as ServerWorld?).graph[index]
        var color = 100000000;
        for (i in neighbors)
        {
            color = min(colorTmp[i].toInt(), color)
        }
        colorTmp.add(Integer(color))
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
        tank.readFromNBT(tag)
        stack = ItemStack.read(tag)
        numberInGraph = tag.getInt("number_in_graph")
        super.read(tag)
    }

    override fun write(tagIn: CompoundNBT): CompoundNBT
    {
        tank.writeToNBT(tagIn)
        stack.write(tagIn)
        tagIn.putInt("number_in_graph", numberInGraph)
        return super.write(tagIn)
    }

    override fun getUpdateTag(): CompoundNBT
    {
        return write(CompoundNBT())
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket
    {
        return UpdatePacket(tank, stack, numberInGraph)
    }

    override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket)
    {
        val packet = pkt as UpdatePacket
        this.numberInGraph = packet.numberInGraph
        this.tank = packet.tank
        this.stack = packet.stack
    }

    override fun <T> getCapability(capability: Capability<T>, facing: Direction?): LazyOptional<T>
    {
        return if (capability === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) holder.cast() else super.getCapability(capability, facing)
    }

    override fun getNumberInGraph(): Int = numberInGraph
    override fun setNumberInGraph(number_in_graph: Int) { numberInGraph = number_in_graph }

    override fun getInput (): FluidTank  = tank
    override fun getOutput(): FluidTank  = tank

    class UpdatePacket(var tank: FluidTank, var stack: ItemStack, var numberInGraph: Int) : SUpdateTileEntityPacket()
    {
        override fun writePacketData(buf: PacketBuffer)
        {
            super.writePacketData(buf)
            SerializeUtils.writeToBuf(tank, buf)
            buf.writeItemStack(stack)
            buf.writeInt(numberInGraph)
        }

        override fun readPacketData(buf: PacketBuffer)
        {
            super.readPacketData(buf)
            tank = SerializeUtils.readTank(buf)
            stack = buf.readItemStack()
            numberInGraph = buf.readInt()
        }
    }
}