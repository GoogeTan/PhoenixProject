package phoenix.utils.block

import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.fml.RegistryObject

abstract class PhoenixTile<T : PhoenixTile<T>> : TileEntity
{
    constructor(tileEntityTypeIn: TileEntityType<T>) : super(tileEntityTypeIn)

    constructor(electricBarrel: RegistryObject<TileEntityType<T>>) : super(electricBarrel.get())

    override fun getUpdateTag(): CompoundNBT = write(CompoundNBT())

    abstract override fun getUpdatePacket(): SUpdateTileEntityPacket
    abstract override fun onDataPacket(net: NetworkManager, pkt: SUpdateTileEntityPacket)
}
