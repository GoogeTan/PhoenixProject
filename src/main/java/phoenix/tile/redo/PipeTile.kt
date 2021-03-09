package phoenix.tile.redo

import net.minecraft.block.BlockState
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import phoenix.init.PhoenixTiles.PIPE
import phoenix.utils.block.PhoenixTile
import phoenix.utils.pipe.IFluidPipe
import java.io.IOException

class PipeTile : PhoenixTile<PipeTile>(PIPE.get()), IFluidPipe
{
    var number_in_graph = 0
    override fun getNumberInGraph(): Int
    {
        return number_in_graph
    }

    override fun setNumberInGraph(number_in_graph: Int)
    {
        this.number_in_graph = number_in_graph
    }

    override fun getBlockState(): BlockState
    {
        return world!!.getBlockState(pos)
    }

    override fun read(tag: CompoundNBT)
    {
        super.read(tag)
        number_in_graph = tag.getInt("number_in_graph")
    }

    override fun write(tag: CompoundNBT): CompoundNBT
    {
        var tag = tag
        tag = super.write(tag)
        tag.putInt("number_in_graph", number_in_graph)
        return tag
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket
    {
        return UpdatePacket(number_in_graph)
    }

    override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket)
    {
        number_in_graph = (pkt as UpdatePacket).numberInGraph
    }

    internal class UpdatePacket(var numberInGraph: Int) : SUpdateTileEntityPacket()
    {
        @Throws(IOException::class)
        override fun readPacketData(buf: PacketBuffer)
        {
            numberInGraph = buf.readInt()
            super.readPacketData(buf)
        }

        @Throws(IOException::class)
        override fun writePacketData(buf: PacketBuffer)
        {
            buf.writeInt(numberInGraph)
            super.writePacketData(buf)
        }
    }
}
