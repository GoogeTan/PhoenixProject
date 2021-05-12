package phoenix.tile.redo

import net.minecraft.item.ItemStack
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.Direction
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import phoenix.init.PhxTiles

open class JuicerTile(type : TileEntityType<out JuicerTile> = PhxTiles.JUICER) : TankTile(type)
{
    open var stack : ItemStack = ItemStack.EMPTY

    override fun getUpdatePacket(): SUpdateTileEntityPacket = SyncPacket()

    inner class SyncPacket : TankTile.SyncPacket()
    {
        override fun readPacketData(buffer : PacketBuffer)
        {
            super.readPacketData(buffer)
            stack = buffer.readItemStack()
        }

        override fun writePacketData(buffer: PacketBuffer)
        {
            super.writePacketData(buffer)
            buffer.writeItemStack(stack)
        }
    }

    override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T>
    {
        if (cap === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of { fluidTank as T }
        return super.getCapability(cap, side)
    }
}