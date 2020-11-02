package phoenix.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeHooks;

public class OvenFuelSlot extends Slot
{
    public OvenFuelSlot(IInventory iInventory, int index, int x, int y)
    {
        super(iInventory, index, x, y);
    }

    public boolean isItemValid(ItemStack stack)
    {
        return ForgeHooks.getBurnTime(stack) > 0;
    }
}

