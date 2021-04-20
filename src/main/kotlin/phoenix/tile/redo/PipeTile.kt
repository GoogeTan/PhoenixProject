package phoenix.tile.redo

import net.minecraft.tileentity.ITickableTileEntity
import phoenix.init.PhoenixTiles.PIPE
import phoenix.tile.AFluidTankTile

class PipeTile(maxCapacity : Int) : AFluidTankTile(PIPE, maxCapacity), ITickableTileEntity
{
    override var needSync: Boolean = false
        get() = false
        set(value) { field = false }
    constructor() : this(1000)
}
