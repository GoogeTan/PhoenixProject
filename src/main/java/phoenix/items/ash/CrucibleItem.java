package phoenix.items.ash;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import phoenix.Phoenix;
import phoenix.init.PhoenixItems;

public class CrucibleItem extends Item
{
    public CrucibleItem()
    {
        super(new Item.Properties().group(Phoenix.PHOENIX).maxStackSize(1));
    }

    @Override
    public boolean hasContainerItem()
    {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack)
    {
        return new ItemStack(PhoenixItems.CRUCIBLE.get());
    }
}
