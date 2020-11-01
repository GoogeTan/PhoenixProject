package phoenix.containers.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import phoenix.Phoenix;
import phoenix.init.PhoenixRecipes;
import phoenix.items.ash.CrucibleItem;
import phoenix.recipes.OvenRecipe;

public class OvenCookingSlot extends Slot
{
    private final PlayerEntity player;
    private int removeCount;

    public OvenCookingSlot(PlayerEntity player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition)
    {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean isItemValid(ItemStack stack)
    {
        return OvenRecipe.recipes_from_inputs.get(stack.getItem()) != null;
    }

    @Override
    public ItemStack decrStackSize(int amount)
    {
        if (this.getHasStack())
        {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack)
    {
        this.onCrafting(stack);
        super.onTake(thePlayer, stack);
        return stack;
    }

    protected void onCrafting(ItemStack stack, int amount)
    {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    @Override
    protected void onCrafting(ItemStack stack)
    {
        stack.onCrafting(this.player.world, this.player, this.removeCount);
        if (!this.player.world.isRemote && this.inventory instanceof AbstractFurnaceTileEntity)
        {
            ((AbstractFurnaceTileEntity) this.inventory).func_213995_d(this.player);
        }

        this.removeCount = 0;
        net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
    }
}
