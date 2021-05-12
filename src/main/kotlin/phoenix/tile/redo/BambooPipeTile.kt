package phoenix.tile.redo

import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidTank
import phoenix.init.PhxTiles
import phoenix.tile.AFluidTile
import phoenix.utils.block.IFluidPipe
import phoenix.utils.getFluid
import phoenix.utils.interactBetweenFluidHandlers

open class BambooPipeTile
    (
    type : TileEntityType<out BambooPipeTile> = PhxTiles.bambooPipe,
    private val capacity: Int = 1000,
    private val pullAmount: Int = 1000
    ) : AFluidTile(type), ITickableTileEntity, IFluidPipe
{
    open var fluidTank : FluidTank = FluidTank(capacity)

    override fun interact(tile: TileEntity, fluid: IFluidHandler): Boolean
    {
        if (tile is IFluidPipe)
        {
            return interactBetweenFluidHandlers(this.getFluid(null).orElse(null), fluid, pullAmount)
        }
        return false
    }

    override fun sync()
    {
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket = SUpdateTileEntityPacket()

    override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T>
    {
        if (cap === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of { fluidTank as T }
        return super.getCapability(cap, side)
    }
}
