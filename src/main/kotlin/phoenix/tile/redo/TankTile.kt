package phoenix.tile.redo

import net.minecraft.network.PacketBuffer
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
import phoenix.network.NetworkHandler
import phoenix.network.SyncTankPacket
import phoenix.tile.AFluidTile
import phoenix.utils.SerializeUtils.readTank
import phoenix.utils.SerializeUtils.writeTank
import phoenix.utils.getFluid
import phoenix.utils.interactBetweenFluidHandlers

open class TankTile
    (
        type : TileEntityType<out TankTile> = PhxTiles.tank,
        val capacity : Int = 5000,
        val pullAmount : Int = 1000
    ) : AFluidTile(type), ITickableTileEntity
{
    open var fluidTank : FluidTank = FluidTank(capacity)

    override fun interact(tile: TileEntity, fluid: IFluidHandler): Boolean
    {
        if (tile is BambooPipeTile)
        {
            return interactBetweenFluidHandlers(this.getFluid(null).orElse(null), fluid, pullAmount)
        }
        return false
    }

    override fun sync()
    {
        NetworkHandler.sendToDim(SyncTankPacket(this), world!!.dimension.type)
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket = this.SyncPacket()

    open inner class SyncPacket : SUpdateTileEntityPacket()
    {
        override fun readPacketData(buffer: PacketBuffer)
        {
            super.readPacketData(buffer)
            fluidTank = buffer.readTank()
        }

        override fun writePacketData(buffer: PacketBuffer)
        {
            super.writePacketData(buffer)
            buffer.writeTank(fluidTank)
        }
    }

    override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T>
    {
        if (cap === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of { fluidTank as T }
        return super.getCapability(cap, side)
    }
}