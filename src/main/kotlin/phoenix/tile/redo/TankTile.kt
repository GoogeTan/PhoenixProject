package phoenix.tile.redo

import net.minecraft.tileentity.ITickableTileEntity
import phoenix.init.PhxTiles
import phoenix.tile.AFluidTankTile

class TankTile(capacity : Int) : AFluidTankTile(PhxTiles.TANK, capacity), ITickableTileEntity
{
    constructor() : this(5000)
    override var needSync: Boolean = false
}