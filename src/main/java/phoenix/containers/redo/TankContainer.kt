package phoenix.containers.redo

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import phoenix.containers.slots.OvenCookingSlot

class TankContainer(id: Int, playerInventory: PlayerInventory?) : Container(PhoenixContainers.TANK.get(), id)
{
    var inventory = Inventory(1)

    init
    {
        addSlot(OvenCookingSlot(playerInventory!!.player, inventory, 0, 60, 60))
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean
    {
        return true;
    }

    companion object Factory
    {
        fun fromNetwork(): ContainerType<TankContainer>
        {
            return ContainerType { id: Int, playerInventoryIn: PlayerInventory? -> TankContainer(id, playerInventoryIn) }
        }
    }
}