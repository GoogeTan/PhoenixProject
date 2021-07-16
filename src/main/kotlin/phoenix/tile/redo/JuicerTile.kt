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

open class JuicerTile(type : TileEntityType<out JuicerTile> = PhxTiles.juicer) : TankTile(type)
{
    open var stack: ItemStack = ItemStack.EMPTY

    override fun readPacketData(buf: PacketBuffer)
    {
        super.readPacketData(buf)
        stack = buf.readItemStack()
    }

    override fun writePacketData(buf: PacketBuffer)
    {
        super.writePacketData(buf)
        buf.writeItemStack(stack)
    }

    override fun <T : Any> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T>
    {
        if (cap === CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return LazyOptional.of { fluidTank as T }
        return super.getCapability(cap, side)
    }
}