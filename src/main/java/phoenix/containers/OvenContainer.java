package phoenix.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.IWorld;
import phoenix.containers.slots.OvenCookingSlot;
import phoenix.containers.slots.OvenFuelSlot;
import phoenix.init.PhoenixContainers;

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
        this(id, playerInventoryIn, new Inventory(5));
    }

    public OvenContainer(int id, PlayerInventory playerInventoryIn, IInventory ovenInventoryIn)
    {
        super(PhoenixContainers.OVEN.get(), id);
        assertInventorySize(ovenInventoryIn, 5);
        this.inventory = ovenInventoryIn;
        this.world = playerInventoryIn.player.world;
        this.addSlot(new OvenCookingSlot(playerInventoryIn.player, ovenInventoryIn, 0, 60, 60));
        this.addSlot(new OvenCookingSlot(playerInventoryIn.player, ovenInventoryIn, 1, 100, 60));
        this.addSlot(new OvenCookingSlot(playerInventoryIn.player, ovenInventoryIn, 2, 60, 100));
        this.addSlot(new OvenCookingSlot(playerInventoryIn.player, ovenInventoryIn, 3, 100, 100));

        this.addSlot(new OvenFuelSlot(ovenInventoryIn, 4, 80, 30));

        for (int i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 173 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 227 + 4));
        }
    }



    @Override
    public boolean canInteractWith(PlayerEntity playerIn)
    {
        return true;
    }
}
