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
import phoenix.utils.ISerializable
import phoenix.utils.SerializeUtils

class OvenContainer(id : Int) : Container(phoenix.init.PhoenixContainers.OVEN.get(), id), ISerializable
{
    lateinit var world : IWorld
    var inventory: Inventory = Inventory(5)

    constructor(id : Int, playerInventoryIn: PlayerInventory) : this(id)
    {
        world = playerInventoryIn.player.world
        addSlot(OvenCookingSlot(inventory, 0, 60, 60))
        addSlot(OvenCookingSlot(inventory, 1, 100, 60))
        addSlot(OvenCookingSlot(inventory, 2, 60, 100))
        addSlot(OvenCookingSlot(inventory, 3, 100, 100))
        addSlot(OvenFuelSlot(inventory, 4, 80, 30))
        for (i in 0..2)
            for (j in 0..8)
                addSlot(Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 173 + i * 18))
        for (k in 0..8)
            addSlot(Slot(playerInventoryIn, k, 8 + k * 18, 227 + 4))
    }

    constructor(id : Int, worldIn : IWorld, nbt: CompoundNBT) : this(id)
    {
        read(nbt)
        world = worldIn
    }

    override fun canInteractWith(playerIn: PlayerEntity): Boolean = true

    override fun write(nbt: CompoundNBT)
    {
        nbt.putInt("slots_count", inventorySlots.size)
        for (slot in inventorySlots)
        {
            nbt.put("slot" + slot.slotIndex, SerializeUtils.serialize(slot))
        }
    }

    override fun read(nbt: CompoundNBT)
    {
        for (i in 0 until nbt.getInt("slots_count"))
        {
            inventorySlots[i] = SerializeUtils.deserializeSlot(nbt, inventory)
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
