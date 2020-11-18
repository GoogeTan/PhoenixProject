package phoenix.containers

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.ContainerType
import net.minecraft.inventory.container.Slot
import net.minecraft.nbt.CompoundNBT
import net.minecraft.world.IWorld
import phoenix.containers.slots.OvenCookingSlot
import phoenix.containers.slots.OvenFuelSlot
import phoenix.tile.ash.OvenTile
import phoenix.utils.ISerializable
import phoenix.utils.SerializeUtils

class OvenContainer(id : Int) : Container(phoenix.init.PhoenixContainers.OVEN.get(), id), ISerializable
{
    lateinit var world : IWorld
    var tile = OvenTile()
    var inventory: Inventory = Inventory(5)

    constructor(id : Int, playerInventoryIn: PlayerInventory) : this(id)
    {
        world = playerInventoryIn.player.world
        addSlot(OvenCookingSlot(inventory, 0, 60,  60))
        addSlot(OvenCookingSlot(inventory, 1, 100, 60))
        addSlot(OvenCookingSlot(inventory, 2, 60,  100))
        addSlot(OvenCookingSlot(inventory, 3, 100, 100))
        addSlot(OvenFuelSlot   (inventory, 4, 80,  30))
        for (i in 0..2)
            for (j in 0..8)
                addSlot(Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 173 + i * 18))
        for (k in 0..8)
            addSlot(Slot(playerInventoryIn, k, 8 + k * 18, 227 + 4))
    }

    override fun onContainerClosed(playerIn : PlayerEntity)
    {
        write(tile.deferredInformation)
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean = true

    override fun write(nbtIn: CompoundNBT)
    {
        val nbt = CompoundNBT();
        for (i in 0..4)
        {
            nbt.put("slot" + inventorySlots[i].slotIndex, SerializeUtils.serialize(inventorySlots[i]))
        }
        nbtIn.put("container", nbt);
    }

    override fun read(nbtIn: CompoundNBT)
    {
        val nbt = nbtIn.getCompound("container")
        for (i in 0..4)
        {
            val current = nbt.getCompound("slot" + i)
            addSlot(SerializeUtils.deserializeSlot(current, inventory))
        }
    }

    companion object
    {
        @JvmStatic
        fun fromNetwork(): ContainerType<OvenContainer>
        {
            return ContainerType { id, playerInventoryIn -> OvenContainer(id, playerInventoryIn) }
        }
    }
}
