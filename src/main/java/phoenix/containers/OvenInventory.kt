package phoenix.containers

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.util.NonNullList
import java.util.stream.Collectors
import kotlin.math.min


open class OvenInventory : IInventory
{
    private val slotsCount: Int
    private val inventoryContents: NonNullList<ItemStack>

    constructor(numSlots: Int)
    {
        slotsCount = numSlots
        inventoryContents = NonNullList.withSize(numSlots, ItemStack.EMPTY)
    }

    constructor(vararg stacksIn: ItemStack)
    {
        slotsCount = stacksIn.size
        inventoryContents = NonNullList.from(ItemStack.EMPTY, *stacksIn)
    }


    /**
     * Returns the stack in the given slot.
     */
    override fun getStackInSlot(index: Int): ItemStack
    {
        return if (index >= 0 && index < inventoryContents.size) inventoryContents[index] else ItemStack.EMPTY
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    override fun decrStackSize(index: Int, count: Int): ItemStack
    {
        val itemstack = ItemStackHelper.getAndSplit(inventoryContents, index, count)
        if (!itemstack.isEmpty)
        {
            markDirty()
        }
        return itemstack
    }
    /*
    fun func_223374_a(item: Item, p_223374_2_: Int): ItemStack
    {
        val itemstack = ItemStack(item, 0)
        for (i in slotsCount - 1 downTo 0)
        {
            val itemstack1 = getStackInSlot(i)
            if (itemstack1.item == item)
            {
                val j = p_223374_2_ - itemstack.count
                val itemstack2 = itemstack1.split(j)
                itemstack.grow(itemstack2.count)
                if (itemstack.count == p_223374_2_)
                {
                    break
                }
            }
        }
        if (!itemstack.isEmpty)
        {
            markDirty()
        }
        return itemstack
    }
    */
    fun addItem(stack: ItemStack): ItemStack
    {
        val itemstack = stack.copy()
        optimiseInventory(itemstack)
        return if (itemstack.isEmpty)
        {
            ItemStack.EMPTY
        } else
        {
            addItemToInventory(itemstack)
            if (itemstack.isEmpty) ItemStack.EMPTY else itemstack
        }
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    override fun removeStackFromSlot(index: Int): ItemStack
    {
        val itemstack = inventoryContents[index]
        return if (itemstack.isEmpty)
        {
            ItemStack.EMPTY
        } else
        {
            inventoryContents[index] = ItemStack.EMPTY
            itemstack
        }
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    override fun setInventorySlotContents(index: Int, stack: ItemStack)
    {
        inventoryContents[index] = stack
        if (!stack.isEmpty && stack.count > this.inventoryStackLimit)
        {
            stack.count = this.inventoryStackLimit
        }
        markDirty()
    }

    override fun markDirty()
    {
    }

    /**
     * Returns the number of slots in the inventory.
     */
    override fun getSizeInventory(): Int
    {
        return slotsCount
    }

    override fun isEmpty(): Boolean
    {
        for (itemstack in inventoryContents)
        {
            if (!itemstack.isEmpty)
            {
                return false
            }
        }
        return true
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    override fun isUsableByPlayer(player: PlayerEntity) = true

    override fun clear()
    {
        inventoryContents.clear()
        markDirty()
    }

    override fun toString(): String
    {
        return inventoryContents.stream().filter { stack: ItemStack -> !stack.isEmpty }.collect(Collectors.toList()).toString()
    }

    private fun addItemToInventory(stack: ItemStack)
    {
        for (i in 0..slotsCount - 1)
        {
            val itemstack = getStackInSlot(i)
            if (itemstack.isEmpty)
            {
                setInventorySlotContents(i, stack.copy())
                stack.count = 0
                return
            }
        }
    }

    private fun optimiseInventory(stack: ItemStack)
    {
        for (i in 0..slotsCount - 1)
        {
            val itemstack = getStackInSlot(i)
            if (ItemStack.areItemsEqual(itemstack, stack))
            {
                uniteStacks(stack, itemstack)
                if (stack.isEmpty)
                {
                    return
                }
            }
        }
    }

    private fun uniteStacks(stack1 : ItemStack, stack2: ItemStack)
    {
        val i = min(this.inventoryStackLimit, stack2.maxStackSize)
        val j = min(stack1.count, i - stack2.count)
        if (j > 0)
        {
            stack2.grow(j)
            stack1.shrink(j)
            markDirty()
        }
    }

    operator fun get(index : Int) : ItemStack
    {
        return inventoryContents[index]
    }

    operator fun set(index : Int, stack : ItemStack)
    {
        inventoryContents[index] = stack
    }
}
