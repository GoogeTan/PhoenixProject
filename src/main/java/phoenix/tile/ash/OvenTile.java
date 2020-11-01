package phoenix.tile.ash;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import phoenix.containers.OvenContainer;
import phoenix.init.PhoenixTiles;
import phoenix.recipes.OvenRecipe;

import javax.annotation.Nullable;
import java.util.List;

public class OvenTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider
{
    int[] timers = new int[4];
    OvenContainer container = null;
    public OvenTile()
    {
        super(PhoenixTiles.OVEN.get());
        timers[0] = 0;
        timers[1] = 0;
        timers[2] = 0;
        timers[3] = 0;
    }

    @Override
    public void tick()
    {
        if (container != null)
        {
            List<Slot> slotList = container.inventorySlots;

            for (int i = 0; i < 4; ++i)
            {
                Slot current = slotList.get(i);
                OvenRecipe recipe = OvenRecipe.recipes_from_inputs.get(current.getStack().getItem());
                if (recipe != null)
                {
                    int cookTime = recipe.getCookTime();
                    try
                    {
                        cookTime++;
                        cookTime--;
                    } catch (Exception e)
                    {
                        cookTime = 40;
                    }
                    timers[i]++;
                    if (timers[i] >= cookTime)
                    {
                        container.putStackInSlot(i, recipe.getResult());
                    }
                } else
                {
                    timers[i] = 0;
                }
            }

        }
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return new StringTextComponent("Oven");
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity)
    {
        if(container == null)
            container = new OvenContainer(id, playerInventory);
        return container;
    }
}
