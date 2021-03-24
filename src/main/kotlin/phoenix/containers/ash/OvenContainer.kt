package phoenix.containers.ash

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import phoenix.init.PhoenixContainers


class OvenContainer : Container(PhoenixContainers.GUIDE.get(), 1015)
{
    var inventory = Inventory(4)

    override fun canInteractWith(playerIn: PlayerEntity) = true
    init
    {
        addSlot(Slot(inventory, 0, 60, 60))
        addSlot(Slot(inventory, 1, 100, 60))
        addSlot(Slot(inventory, 2, 60, 100))
        addSlot(Slot(inventory, 3, 100, 100))
    }

    var size = inventorySlots.size
    operator fun get(int : Int): ItemStack = getSlot(int).stack
    operator fun set(int : Int, stack : ItemStack) = getSlot(int).putStack(stack)
}