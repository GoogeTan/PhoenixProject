package phoenix.tile.redo

import net.minecraft.tileentity.ITickableTileEntity
import phoenix.init.PhoenixTiles
import phoenix.tile.AFluidTankTile

class TankTile(capacity : Int) : AFluidTankTile(PhoenixTiles.TANK, capacity), ITickableTileEntity
{
    constructor() : this(5000)
    override var needSync: Boolean = false
}