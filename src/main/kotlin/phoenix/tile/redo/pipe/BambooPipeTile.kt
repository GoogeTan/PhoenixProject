package phoenix.tile.redo.pipe

import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.state.IProperty
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import phoenix.init.PhxTiles
import phoenix.tile.FluidTileSidable

open class BambooPipeTile
    (
    type : TileEntityType<out BambooPipeTile> = PhxTiles.bambooPipe,
    capacity: Int = 1000,
    facing : IProperty<Direction> = BlockStateProperties.HORIZONTAL_FACING,
    pullAmount: Int = 1000 / 5
    ) : FluidTileSidable(type, facing, pullAmount)
{
    override var needSync: Boolean = false
        get() = false
        set(value) { field = false }

    override fun sync()
    {
    }
}
