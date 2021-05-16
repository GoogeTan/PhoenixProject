package phoenix.tile

import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.fluids.capability.IFluidHandler
import phoenix.utils.block.PhoenixTile
import phoenix.utils.getFluid
import phoenix.utils.getTileAt

abstract class AFluidTile(type: TileEntityType<out AFluidTile>) : PhoenixTile(type), ITickableTileEntity, ISyncable
{
    override var needSync : Boolean = false

    override fun tick()
    {
        val world = world
        if(world != null && !world.isRemote)
        {
            for (i in Direction.values())
            {
                val tile = world.getTileAt<TileEntity>(pos.offset(i))
                if (tile != null)
                {
                    val handler = tile.getFluid(i.opposite)
                    if(handler.isPresent)
                    {
                        val fluid = handler.orElseThrow { NullPointerException("Present fluid tank in not present! It sound like bread, but it is reality.") }
                        needSync = needSync or interact(tile, fluid, i.opposite)
                    }
                }
            }

            if (needSync)
            {
                sync()
                needSync = false
            }
        }
    }

    abstract fun interact(tile : TileEntity, fluid : IFluidHandler, side : Direction) : Boolean

    abstract fun sync()

    override fun getUpdateTag(): CompoundNBT = write(super.getUpdateTag())
}
