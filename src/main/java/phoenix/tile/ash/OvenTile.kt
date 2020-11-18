package phoenix.tile.ash

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.NetworkManager
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.server.SUpdateTileEntityPacket
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import phoenix.containers.OvenContainer
import phoenix.init.PhoenixTiles
import phoenix.recipes.OvenRecipe
import phoenix.utils.block.PhoenixTile

class OvenTile : PhoenixTile(PhoenixTiles.OVEN.get()), ITickableTileEntity, INamedContainerProvider
{
    var deferredInformation = CompoundNBT()
    var timers = IntArray(4)
    var container: OvenContainer? = null

    init
    {
        timers[0] = 0
        timers[1] = 0
        timers[2] = 0
        timers[3] = 0
    }
    
    override fun tick()
    {
        if (container != null)
        {
            val slotList = container!!.inventorySlots
            for (i in 0..3)
            {
                val current = slotList[i]
                val recipe = OvenRecipe.recipes_from_inputs[current.stack.item]
                if (recipe != null)
                {
                    val cookTime = recipe.cookTime
                    timers[i]++
                    if (timers[i] >= cookTime)
                    {
                        container!!.putStackInSlot(i, recipe.result)
                    }
                }
                else
                {
                    timers[i] = 0
                }
            }
        }
    }

    override fun createMenu(id: Int, playerInventory: PlayerInventory, playerEntity: PlayerEntity): Container?
    {
        if (container == null)
        {
            container = OvenContainer(id, playerInventory)
            if(deferredInformation.contains("container"))
            {
                container?.read(deferredInformation)
            }
        }
        container?.tile = this
        return container
    }

    override fun getDisplayName(): ITextComponent = StringTextComponent("Oven")

    override fun write(compound: CompoundNBT): CompoundNBT?
    {
        compound.putIntArray("timers", timers)
        if(container != null)
            (container)?.write(compound)
        return super.write(compound)
    }

    override fun getUpdatePacket(): SUpdateTileEntityPacket = UpdatePacket(timers)

    override fun onDataPacket(net: NetworkManager?, pkt: SUpdateTileEntityPacket?)
    {
        timers = (pkt as UpdatePacket).timers
    }

    override fun read(compound: CompoundNBT)
    {
        timers = compound.getIntArray("timers")
        val info = compound.get("container")
        if(info != null)
            deferredInformation.put("container", info)

        super.read(compound)
    }


    class UpdatePacket(var timers: IntArray) : SUpdateTileEntityPacket()
    {
        override fun writePacketData(buf: PacketBuffer)
        {
            super.writePacketData(buf)
            buf.writeVarIntArray(timers)
        }

        override fun readPacketData(buf: PacketBuffer)
        {
            super.readPacketData(buf)
            timers = buf.readVarIntArray()
        }
    }
}