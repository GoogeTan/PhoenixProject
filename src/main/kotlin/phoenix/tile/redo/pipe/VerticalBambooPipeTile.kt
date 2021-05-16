package phoenix.tile.redo.pipe

import net.minecraft.block.HorizontalBlock
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
import phoenix.utils.getTileAt
import phoenix.utils.interactBetweenPipes

open class VerticalBambooPipeTile
    (
        type : TileEntityType<out BambooPipeTile> = PhxTiles.bambooPipe,
        private val capacity: Int = 1000,
        private val pullAmount: Int = 1000
    ) : AFluidTile(type), ITickableTileEntity, IFluidPipe
{
    open var fluidTank : FluidTank = FluidTank(capacity)

    override fun tick()
    {
        val world = world
        if(world != null && !world.isRemote)
        {
            val input = blockState[HorizontalBlock.HORIZONTAL_FACING]
            val tile = world.getTileAt<TileEntity>(pos.offset(input))
            if (tile != null)
            {
                val handler = tile.getFluid(input.opposite)
                if (handler.isPresent)
                {
                    val fluid =
                        handler.orElseThrow { NullPointerException("Present fluid tank in not present! It sound like bread, but it is reality.") }
                    needSync = needSync or interact(tile, fluid, input.opposite)
                }
            }
            val output = Direction.DOWN
            if (tile != null)
            {
                val handler = tile.getFluid(output.opposite)
                if (handler.isPresent)
                {
                    val fluid = handler.orElseThrow { NullPointerException("Present fluid tank in not present! It sound like bread, but it is reality.") }
                    needSync = needSync or interact(tile, fluid, output.opposite)
                }
            }

            if (needSync)
            {
                sync()
                needSync = false
            }
        }
    }

    override fun interact(tile: TileEntity, fluid: IFluidHandler, side : Direction): Boolean
    {
        if (tile is IFluidPipe)
        {
            val tfluid = this.getFluid(side)
            if(tfluid.isPresent)
                return interactBetweenPipes(tfluid.orElse(null), fluid, pullAmount)
        }
        return false
    }

    override fun sync()
    {
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket = SUpdateTileEntityPacket()

    override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T>
    {
        return if (cap === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) LazyOptional.of { fluidTank as T } else super.getCapability(cap, side)
    }
}
