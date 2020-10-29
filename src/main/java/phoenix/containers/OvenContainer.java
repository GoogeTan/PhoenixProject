package phoenix.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IWorld;
import phoenix.Phoenix;
import phoenix.client.gui.OvenScreen;
import phoenix.containers.slots.OvenCookingSlot;
import phoenix.containers.slots.OvenFuelSlot;
import phoenix.init.PhoenixContainers;

import javax.annotation.Nullable;

import static net.minecraft.item.crafting.IRecipeType.SMELTING;

public class OvenContainer extends Container
{
    IWorld world;
    IInventory inventory;

    public static OvenContainer fromNetwork(int id, PlayerInventory inventory)
    {
        return new OvenContainer(id, inventory);
    }

    public OvenContainer(int id, PlayerInventory playerInventoryIn)
    {
        this(id, playerInventoryIn, new Inventory(3));
    }

    public OvenContainer(int id, PlayerInventory playerInventoryIn, IInventory ovenInventoryIn)
    {
        super(PhoenixContainers.OVEN.get(), id);
        assertInventorySize(ovenInventoryIn, 3);
        this.inventory = ovenInventoryIn;
        this.world = playerInventoryIn.player.world;
        this.addSlot(new Slot(ovenInventoryIn, 0, 56, 17));
        this.addSlot(new OvenFuelSlot(ovenInventoryIn, 1, 56, 53));
        this.addSlot(new OvenCookingSlot(playerInventoryIn.player, ovenInventoryIn, 2, 116, 35));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return true;
    }
}
