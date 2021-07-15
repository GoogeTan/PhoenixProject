package phoenix.blocks.ash

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.util.INBTSerializable
import phoenix.recipes.OvenRecipe
import phoenix.utils.SizableInventory

open class OvenData(size : Int) : SizableInventory(size), INBTSerializable<CompoundNBT>
{
    protected open var timers : IntArray = IntArray(size)
    protected open var burnTime: Int = 0
    protected open var maxBurnTime : Int = 1200

    init
    {
        addListener {
            val time = ForgeHooks.getBurnTime(getStackInSlot(4))
            if (time > 0)
                addTime(time);
            setInventorySlotContents(4, ItemStack.EMPTY)
        }
    }

    constructor(inv : IInventory, timers : IntArray, burnTime : Int = 0, maxBurnTime : Int = 0) : this(timers.size)
    {
        for (i in 0 until this.sizeInventory)
            setInventorySlotContents(i, inv.getStackInSlot(i))
        this.timers = timers;
        this.burnTime = burnTime
        this.maxBurnTime = maxBurnTime
    }

    constructor(tag : CompoundNBT) : this(tag.getInt("size"))
    {
        deserializeNBT(tag)
    }

    private fun addTime(time : Int)
    {
        burnTime += time;
        burnTime = Integer.min(maxBurnTime, burnTime)
        burnTime = Integer.max(0, burnTime)
    }

    operator fun get(i : Int): ItemStack = getStackInSlot(i)
    operator fun set(i : Int, stack : ItemStack)
    {
        setInventorySlotContents(i, stack)
        if (stack.isEmpty)
            timers[i] = 0
    }

    infix fun replace(other : OvenData)
    {
        this.timers = other.timers
        this.burnTime = other.burnTime
        this.maxBurnTime = other.maxBurnTime
        this.resize(timers.size)
        for (i in 1 until sizeInventory)
            this[i] = other[i]
    }

    fun isBurning() = burnTime > 0

    fun tickFire()
    {
        burnTime--
        burnTime = Integer.min(maxBurnTime, burnTime)
        burnTime = Integer.max(0, burnTime)
    }

    fun tickItems() : Boolean
    {
        var has = false
        for (i in 1 until sizeInventory)
        {
            val current = this[i]
            if (OvenRecipe.recipesFromInputs.contains(current.item))
            {
                val recipe = OvenRecipe.recipesFromInputs[current.item]!!
                val cookTime: Int = recipe.cookTime
                timers[i]++
                if (timers[i] >= cookTime)
                {
                    this[i] = recipe.result
                    has = true
                    timers[i] = 0
                }
            }
        }
        return has
    }

    override fun serializeNBT(): CompoundNBT
    {
        val res = CompoundNBT()
        res.putInt("size", timers.size)
        res.putIntArray("timers", timers)
        res.putInt("burn_time", burnTime)

        val nbt = CompoundNBT()
        for (index in 1 until sizeInventory)
        {
            val stack = CompoundNBT()
            this[index].write(stack)
            nbt.put("slot$index", stack)
        }

        res.put("data", nbt)

        return res;
    }

    override fun deserializeNBT(tag: CompoundNBT)
    {
        timers = tag.getIntArray("timers")
        burnTime = tag.getInt("burn_time")
        val nbt = tag.getCompound("data")
        for (i in 1 until sizeInventory)
        {
            this[i] = ItemStack.read(nbt.getCompound("slot$i"))
        }
        addListener {
            val time = ForgeHooks.getBurnTime(getStackInSlot(4))
            if (time > 0)
                addTime(time);
            setInventorySlotContents(4, ItemStack.EMPTY)
        }
    }

    fun writeToBuf(buf : PacketBuffer)
    {
        buf.writeInt(sizeInventory)
        buf.writeVarIntArray(timers)
        buf.writeInt(burnTime)
        buf.writeInt(maxBurnTime)
        for (i in 1 until sizeInventory)
            buf.writeItemStack(this[i])
    }

    fun readFromBuf(buf : PacketBuffer)
    {
        resize(buf.readInt())
        timers = buf.readVarIntArray()
        burnTime = buf.readInt()
        maxBurnTime = buf.readInt()
        for (i in 1 until sizeInventory)
            this[i] = buf.readItemStack()
    }
}