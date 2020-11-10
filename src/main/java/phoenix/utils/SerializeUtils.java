package phoenix.utils;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
}