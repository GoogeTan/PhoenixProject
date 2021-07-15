package phoenix.utils

import com.google.common.collect.Lists
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.IInventoryChangedListener
import net.minecraft.inventory.IRecipeHelperPopulator
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.RecipeItemHelper
import java.util.stream.Collectors

open class SizableInventory : IInventory, IRecipeHelperPopulator
{
    private val slotsCount: Int
        get() = inventoryContents.size
    private val inventoryContents: SizedArrayList<ItemStack>
    private var listeners: MutableList<IInventoryChangedListener>? = null

    constructor(numSlots: Int)
    {
        inventoryContents = SizedArrayList(numSlots, ItemStack.EMPTY)
    }

    constructor(vararg stacksIn: ItemStack)
    {
        inventoryContents = SizedArrayList.of(*stacksIn)
    }

    /**
     * Add a listener that will be notified when any item in this inventory is modified.
     */
    fun addListener(listener: IInventoryChangedListener)
    {
        if (listeners == null)
        {
            listeners = Lists.newArrayList()
        }
        listeners!!.add(listener)
    }

    /**
     * removes the specified IInvBasic from receiving further change notices
     */
    fun removeListener(listener: IInventoryChangedListener)
    {
        listeners?.remove(listener)
    }

    /**
     * Returns the stack in the given slot.
     */
    override fun getStackInSlot(index: Int): ItemStack
    {
        return if (index >= 0 && index < inventoryContents.size) inventoryContents[index] else ItemStack.EMPTY
    }

    protected fun resize(size : Int)
    {
        if (size == slotsCount || size < 0)
            return;
        while (size > slotsCount)
            inventoryContents.add(ItemStack.EMPTY)
        while (size < slotsCount)
            inventoryContents.removeLast()
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    override fun decrStackSize(index: Int, count: Int): ItemStack
    {
        val itemstack = ItemStackHelper.getAndSplit(inventoryContents, index, count)
        if (!itemstack.isEmpty())
        {
            markDirty()
        }
        return itemstack
    }

    fun func_223374_a(p_223374_1_: Item, p_223374_2_: Int): ItemStack
    {
        val itemstack = ItemStack(p_223374_1_, 0)
        for (i in slotsCount - 1 downTo 0)
        {
            val itemstack1 = getStackInSlot(i)
            if (itemstack1.getItem() == p_223374_1_)
            {
                val j = p_223374_2_ - itemstack.getCount()
                val itemstack2 = itemstack1.split(j)
                itemstack.grow(itemstack2.getCount())
                if (itemstack.getCount() == p_223374_2_)
                {
                    break
                }
            }
        }
        if (!itemstack.isEmpty())
        {
            markDirty()
        }
        return itemstack
    }

    fun addItem(stack: ItemStack): ItemStack
    {
        val itemstack = stack.copy()
        func_223372_c(itemstack)
        return if (itemstack.isEmpty())
        {
            ItemStack.EMPTY
        } else
        {
            func_223375_b(itemstack)
            if (itemstack.isEmpty()) ItemStack.EMPTY else itemstack
        }
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    override fun removeStackFromSlot(index: Int): ItemStack
    {
        val itemstack = inventoryContents[index]
        return if (itemstack.isEmpty())
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
        if (!stack.isEmpty() && stack.getCount() > this.inventoryStackLimit)
        {
            stack.setCount(this.inventoryStackLimit)
        }
        markDirty()
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
            if (!itemstack.isEmpty())
            {
                return false
            }
        }
        return true
    }

    /**
     * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't think it
     * hasn't changed and skip it.
     */
    override fun markDirty()
    {
        if (listeners != null)
        {
            for (iinventorychangedlistener in listeners!!)
            {
                iinventorychangedlistener.onInventoryChanged(this)
            }
        }
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    override fun isUsableByPlayer(player: PlayerEntity): Boolean
    {
        return true
    }

    override fun clear()
    {
        inventoryContents.clear()
        markDirty()
    }

    override fun fillStackedContents(helper: RecipeItemHelper)
    {
        for (itemstack in inventoryContents)
        {
            helper.accountStack(itemstack)
        }
    }

    override fun toString(): String
    {
        return inventoryContents.stream().filter { p_223371_0_: ItemStack -> !p_223371_0_.isEmpty() }
            .collect(Collectors.toList()).toString()
    }

    private fun func_223375_b(p_223375_1_: ItemStack)
    {
        for (i in 0 until slotsCount)
        {
            val itemstack = getStackInSlot(i)
            if (itemstack.isEmpty())
            {
                setInventorySlotContents(i, p_223375_1_.copy())
                p_223375_1_.setCount(0)
                return
            }
        }
    }

    private fun func_223372_c(stack: ItemStack)
    {
        for (i in 0 until slotsCount)
        {
            val itemstack = getStackInSlot(i)
            if (ItemStack.areItemsEqual(itemstack, stack))
            {
                func_223373_a(stack, itemstack)
                if (stack.isEmpty())
                {
                    return
                }
            }
        }
    }

    private fun func_223373_a(p_223373_1_: ItemStack, p_223373_2_: ItemStack)
    {
        val i = Math.min(this.inventoryStackLimit, p_223373_2_.maxStackSize)
        val j = Math.min(p_223373_1_.getCount(), i - p_223373_2_.getCount())
        if (j > 0)
        {
            p_223373_2_.grow(j)
            p_223373_1_.shrink(j)
            markDirty()
        }
    }
}