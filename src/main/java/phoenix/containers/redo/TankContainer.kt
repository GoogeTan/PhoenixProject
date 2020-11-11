package phoenix.containers.redo

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import phoenix.containers.slots.OvenCookingSlot
import phoenix.init.PhoenixContainers

class TankContainer(id: Int) : Container(PhoenixContainers.TANK.get(), id)
{
    var inventory = Inventory(1)

    init
    {
        addSlot(OvenCookingSlot(inventory, 0, 60, 60))
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean = true;


    companion object Factory
    {
        @JvmStatic
        fun fromNetwork(): ContainerType<TankContainer>
        {
            return ContainerType { id: Int, _: PlayerInventory? -> TankContainer(id) }
        }
    }
}