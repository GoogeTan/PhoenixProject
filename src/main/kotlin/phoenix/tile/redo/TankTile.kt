package phoenix.tile.redo

import net.minecraft.state.IProperty
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import phoenix.init.PhxTiles
import phoenix.network.SyncTankPacket
import phoenix.network.sendToDimension
import phoenix.tile.FluidTileSidable

open class TankTile
    (
        type : TileEntityType<out TankTile> = PhxTiles.tank,
        facing : IProperty<Direction> = BlockStateProperties.FACING,
        capacity : Int = 5000,
        pullAmount : Int = 1000
    ) : FluidTileSidable(type, facing, pullAmount, capacity), ITickableTileEntity
{
    override var needSync: Boolean = true

    override fun sync() = SyncTankPacket(pos, this.fluidTank).sendToDimension(world!!.dimension.type)
}