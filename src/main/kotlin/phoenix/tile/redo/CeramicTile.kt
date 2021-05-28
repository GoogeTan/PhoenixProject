package phoenix.tile.redo

import net.minecraft.block.material.MaterialColor
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import phoenix.init.PhxTiles

class CeramicTile
    (
    tileEntityTypeIn: TileEntityType<out CeramicTile> = PhxTiles.ceramic,
    var colour: Int = MaterialColor.RED_TERRACOTTA.colorValue,
    var hardness: Float = 3.0f,
    var resistance: Float = 3.0f
    ) : TileEntity(tileEntityTypeIn)
