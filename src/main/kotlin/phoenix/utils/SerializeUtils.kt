package phoenix.utils

import com.google.gson.JsonObject
import net.minecraft.inventory.Inventory
import net.minecraft.inventory.container.Slot
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.ShapedRecipe
import net.minecraft.nbt.CompoundNBT
import net.minecraft.network.PacketBuffer
import net.minecraft.util.JSONUtils
import net.minecraft.util.ResourceLocation
import net.minecraft.util.registry.Registry
import net.minecraftforge.fluids.capability.templates.FluidTank


object SerializeUtils
{
    fun serialize(slot: Slot): CompoundNBT
    {
        val res = CompoundNBT()
        res.putInt("index", slot.slotIndex)
        res.putInt("x", slot.xPos)
        res.putInt("y", slot.yPos)
        res.putString("class", slot.javaClass.toGenericString())
        slot.stack.write(res)
        return res
    }

    fun deserializeSlot(nbt: CompoundNBT, inventory: Inventory?): Slot
    {
        val index = nbt.getInt("index")
        val x = nbt.getInt("x")
        val y = nbt.getInt("y")
        val cLass = nbt.getString("class")
        val stack = ItemStack.read(nbt)
        val slot = Slot(inventory, index, x, y)
        slot.putStack(stack)
        return slot
    }

    fun JsonObject.readItemStack(name: String): ItemStack
    {
        return if (this[name].isJsonObject) ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(this, name)) else
        {
            val s1 = JSONUtils.getString(this, name)
            val resourcelocation = ResourceLocation(s1)
            ItemStack(Registry.ITEM.getValue(resourcelocation).orElseThrow {
                IllegalStateException(
                    "Item: $s1 does not exist"
                )
            })
        }
    }

    fun PacketBuffer.writeTank(tank: FluidTank)
    {
        this.writeFluidStack(tank.fluid)
        this.writeInt(tank.capacity)
    }

    fun PacketBuffer.readTank(): FluidTank
    {
        val stack = this.readFluidStack()
        val capacity = this.readInt()

        val res = FluidTank(capacity)
        res.fluid = stack
        return res
    }
}