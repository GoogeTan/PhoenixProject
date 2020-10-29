package phoenix.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.world.IWorld;

import javax.annotation.Nullable;

public class OvenContainer extends Container
{
    IWorld world;
    IInventory inventory;
    IRecipeType recipeType;
    public OvenContainer(ContainerType<?> containerTypeIn, IRecipeType recipeTypeIn, int id, PlayerInventory playerInventoryIn, IInventory ovenInventoryIn, IIntArray furnaceDataIn) {
        super(containerTypeIn, id);
        this.recipeType = recipeTypeIn;
        assertInventorySize(ovenInventoryIn, 3);
        assertIntArraySize(furnaceDataIn, 4);
        this.inventory = ovenInventoryIn;
        this.world = playerInventoryIn.player.world;
        this.addSlot(new Slot(ovenInventoryIn, 0, 56, 17));
        this.addSlot(new FurnaceFuelSlot(this, ovenInventoryIn, 1, 56, 53));
        this.addSlot(new FurnaceResultSlot(playerInventoryIn.player, ovenInventoryIn, 2, 116, 35));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 142));
        }

        this.trackIntArray(furnaceDataIn);
    }

    protected OvenContainer(@Nullable ContainerType<?> type, int id)
    {
        super(type, id);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return false;
    }
}
