package phoenix.tile.redo

import com.mojang.datafixers.util.Pair
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.init.PhoenixTiles
import phoenix.utils.block.PhoenixTile
import phoenix.utils.graph.GraphNode
import phoenix.utils.pipe.IFluidMechanism
import java.util.*

class ElectricBarrelTile : PhoenixTile<ElectricBarrelTile>(PhoenixTiles.ELECTRIC_BARREL.get()), ITickableTileEntity, IFluidMechanism
{
    override fun tick()
    {
        TODO("Not yet implemented")
    }

    override fun getNumberInGraph(): Int
    {
        TODO("Not yet implemented")
    }

    override fun setNumberInGraph(number_in_graph: Int)
    {
        TODO("Not yet implemented")
    }

    override fun getInput(): FluidTank
    {
        TODO("Not yet implemented")
    }

    override fun getOutput(): FluidTank
    {
        TODO("Not yet implemented")
    }

    override fun removeMechanismByIndex(index: Int)
    {
        TODO("Not yet implemented")
    }

    override fun addMechanismByIndex(world: ServerWorld?, index: Int)
    {
        TODO("Not yet implemented")
    }

    override fun getConnectedMechanisms(numberInGraph: Int): Pair<ArrayList<GraphNode>, ArrayList<GraphNode>>
    {
        TODO("Not yet implemented")
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket
    {
        TODO("Not yet implemented")
    }

    override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket)
    {
        TODO("Not yet implemented")
    }
}