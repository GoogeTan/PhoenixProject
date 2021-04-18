package phoenix.utils.block

import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType

abstract class PhoenixTile<T : PhoenixTile<T>>(tileEntityTypeIn: TileEntityType<T>) : TileEntity(tileEntityTypeIn)
{

    override fun getUpdateTag(): CompoundNBT = write(CompoundNBT())

    abstract override fun getUpdatePacket(): SUpdateTileEntityPacket
    abstract override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket)
}
