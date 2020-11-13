package phoenix.utils;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import phoenix.containers.slots.OvenCookingSlot;
import phoenix.containers.slots.OvenFuelSlot;

public class SerializeUtils
{
    private static final String ovenCookingSlotClass = OvenCookingSlot.class.toGenericString();
    private static final String ovenFuelSlotClass = OvenFuelSlot.class.toGenericString();
    public static CompoundNBT serialize(Slot slot)
    {
        CompoundNBT res = new CompoundNBT();
        res.putInt("index", slot.getSlotIndex());
        res.putInt("x", slot.xPos);
        res.putInt("y", slot.yPos);
        res.putString("class", slot.getClass().toGenericString());
        slot.getStack().write(res);
        return res;
    }

    public static Slot deserializeSlot(CompoundNBT nbt, Inventory inventory)
    {
        int index = nbt.getInt("index");
        int x = nbt.getInt("x");
        int y = nbt.getInt("y");
        String cLass = nbt.getString("class");
        ItemStack stack = ItemStack.read(nbt);
        if (ovenCookingSlotClass.equals(cLass))
        {
            Slot slot = new OvenCookingSlot(inventory, index, x, y);
            slot.putStack(stack);
            return slot;
        }
        else if(ovenFuelSlotClass.equals(cLass))
        {
            Slot slot = new OvenFuelSlot(inventory, index, x, y);
            slot.putStack(stack);
            return slot;
        }
        else
        {
            Slot slot = new Slot(inventory, index, x, y);
            slot.putStack(stack);
            return slot;
        }
    }

    public static ItemStack readItemStack(JsonObject json, String name)
    {
        ItemStack itemstack;
        if (json.get(name).isJsonObject())
            itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, name));
        else
        {
            String s1 = JSONUtils.getString(json, name);
            ResourceLocation resourcelocation = new ResourceLocation(s1);
            itemstack = new ItemStack(Registry.ITEM.getValue(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + s1 + " does not exist")));
        }
        return itemstack;
    }

    public static void writeToBuf(FluidTank tank, PacketBuffer buf)
    {
        tank.getFluid().writeToPacket(buf);
        buf.writeInt(tank.getCapacity());
    }

    public static FluidTank readTank(PacketBuffer buf)
    {
        FluidStack stack = buf.readFluidStack();
        int capacity = buf.readInt();
        FluidTank res = new FluidTank(capacity);
        res.setFluid(stack);
        return res;
    }
}