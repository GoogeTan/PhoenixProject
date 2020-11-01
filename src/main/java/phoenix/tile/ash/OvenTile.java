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
        if(container != null)
        {
            List<Slot> slotList = container.inventorySlots;

            for (int i = 1; i < slotList.size(); ++i)
            {
                Slot current = slotList.get(i);
                if(!current.getStack().isEmpty() || !OvenRecipe.inputs.contains(current.getStack().getItem()))
                {
                    OvenRecipe recipe = OvenRecipe.recipes_from_inputs.get(current.getStack().getItem());
                    timers[i - 1]++;
                    if(timers[i - 1] >= recipe.getCookTime())
                    {
                        container.putStackInSlot(i, recipe.getResult());
                    }
                }
                else
                {
                    timers[i - 1] = 0;
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
