package phoenix.tile.redo.pipe

import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import phoenix.api.block.IFluidPipe
import phoenix.init.PhxTiles
import phoenix.other.MutablePair
import phoenix.other.next
import phoenix.other.uniquePairOf
import phoenix.tile.FluidTileSidable

open class TurnBambooPipeTile
    (
    type : TileEntityType<out TurnBambooPipeTile> = PhxTiles.turnBambooPipe,
    capacity: Int = 1000,
    pullAmount: Int = 1000
) : FluidTileSidable(type, BlockStateProperties.HORIZONTAL_FACING, pullAmount, capacity), ITickableTileEntity,
    IFluidPipe
{
    override var needSync: Boolean = false
        get() = false
        set(value) { field = false }

    override fun sync()
    {
    }

    override fun getDirections(): MutablePair<Direction?, Direction?> = uniquePairOf(null, blockState[facing].next())
}