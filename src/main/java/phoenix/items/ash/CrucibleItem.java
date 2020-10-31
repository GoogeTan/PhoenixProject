package phoenix.items.ash;

import net.minecraft.item.Item;
import phoenix.Phoenix;

public class CrucibleItem extends Item
{
    public CrucibleItem()
    {
        super(new Item.Properties().group(Phoenix.PHOENIX).maxStackSize(8));
    }

    public CrucibleItem(Item.Properties properties)
    {
        super(properties.group(Phoenix.PHOENIX).maxStackSize(8));
    }
}
