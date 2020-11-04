package phoenix.containers.slots;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.ForgeHooks;

public class OvenFuelSlot extends Slot
{
    public OvenFuelSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack)
    {
        return ForgeHooks.getBurnTime(stack) > 0 || isLavaBucket(stack);
    }

    public int getItemStackLimit(ItemStack stack) {
        return isLavaBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }

    public static boolean isLavaBucket(ItemStack stack) {
        return stack.getItem() == Items.LAVA_BUCKET;
    }
}

