package phoenix.tile.redo;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import phoenix.init.PhoenixTiles;
import phoenix.utils.block.PhoenixTile;
import phoenix.utils.pipe.IFluidPipe;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PipeTile extends PhoenixTile<PipeTile> implements IFluidPipe
{
    int number_in_graph = 0;

    public PipeTile()
    {
        super(PhoenixTiles.getPIPE().get());
    }

    @Override
    public int getNumberInGraph()
    {
        return number_in_graph;
    }

    @Override
    public void setNumberInGraph(int number_in_graph)
    {
        this.number_in_graph = number_in_graph;
    }

    public BlockState getBlockState()
    {
        return world.getBlockState(pos);
    }

    @Override
    public void read(CompoundNBT tag)
    {
        super.read(tag);
        number_in_graph = tag.getInt("number_in_graph");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        tag = super.write(tag);
        tag.putInt("number_in_graph", number_in_graph);
        return tag;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new UpdatePacket(number_in_graph);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        number_in_graph = ((UpdatePacket) pkt).numberInGraph;
    }

    static class UpdatePacket extends SUpdateTileEntityPacket
    {
        int numberInGraph;

        public UpdatePacket(int numberInGraph)
        {
            this.numberInGraph = numberInGraph;
        }

        @Override
        public void readPacketData(PacketBuffer buf) throws IOException
        {
            numberInGraph = buf.readInt();
            super.readPacketData(buf);
        }

        @Override
        public void writePacketData(PacketBuffer buf) throws IOException
        {
            buf.writeInt(numberInGraph);
            super.writePacketData(buf);
        }
    }
}
