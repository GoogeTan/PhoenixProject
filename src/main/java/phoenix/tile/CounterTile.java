package phoenix.tile;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import phoenix.init.PhoenixTiles;
import phoenix.utils.block.PhoenixTile;

import java.io.IOException;

public class CounterTile extends PhoenixTile
{
    private int count = 0;
    public CounterTile()
    {
        super(PhoenixTiles.OVEN.get());
    }

    public void incCount()
    {
        count++;
    }

    public int getCount()
    {
        return count;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new UpdatePacket(count);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        count = ((UpdatePacket) pkt).count;
    }

    @Override
    public void read(CompoundNBT nbt)
    {
        count = nbt.getInt("count");
        super.read(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt)
    {
        nbt.putInt("count", count);
        return super.write(nbt);
    }

    static class UpdatePacket extends SUpdateTileEntityPacket
    {
        public int count;

        public UpdatePacket(int countIn)
        {
            this.count = countIn;
        }

        @Override
        public void readPacketData(PacketBuffer buffer) throws IOException
        {
            buffer.writeInt(count);
            super.readPacketData(buffer);
        }

        @Override
        public void writePacketData(PacketBuffer buffer) throws IOException
        {
            count = buffer.readInt();
            super.writePacketData(buffer);
        }
    }
}
