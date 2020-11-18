package phoenix.tile.ash;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import phoenix.init.PhoenixTiles;
import phoenix.utils.block.PhoenixTile;

import java.io.IOException;

public class PotteryBarrelTile extends PhoenixTile
{
    public int jumpsCount = 0;
    public PotteryBarrelTile()
    {
        super(PhoenixTiles.POTTERY_BARREL.get());
    }

    @Override
    public void read(CompoundNBT compound)
    {
        super.read(compound);
        jumpsCount = compound.getInt("jumpscount");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {

        compound.putInt("jumpscount", jumpsCount);
        return super.write(compound);
    }

    public void incrementJumpsCount()
    {
        jumpsCount++;
        jumpsCount = Math.min(jumpsCount, 200);
    }

    public void nullifyJumpsCount()
    {
        jumpsCount = 0;
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket()
    {
        return new UpdatePacket(jumpsCount);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt)
    {
        jumpsCount = ((UpdatePacket) pkt).jumpsCount;
    }

    static class UpdatePacket extends SUpdateTileEntityPacket
    {
        int jumpsCount;

        public UpdatePacket(int jumpsCount)
        {
            this.jumpsCount = jumpsCount;
        }

        @Override
        public void readPacketData(PacketBuffer buf) throws IOException
        {
            super.readPacketData(buf);
            jumpsCount = buf.readInt();
        }

        @Override
        public void writePacketData(PacketBuffer buf) throws IOException
        {
            super.writePacketData(buf);
            buf.writeInt(jumpsCount);
        }
    }
}
