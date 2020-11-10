package phoenix;

import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;

import java.util.Comparator;

public class PhoenixGroup extends ItemGroup
{
    public PhoenixGroup(final String name)
    {
        super(name);
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(Item.getItemFromBlock(Blocks.END_PORTAL_FRAME));
    }

    @Override
    public boolean hasSearchBar()
    {
        return true;
    }

    @Override
    public void fill(NonNullList<ItemStack> items)
    {
        items.sort(new comp());
        super.fill(items);
    }

    @Override
    public int getSlotColor() {
        return 0;
    }

    static class comp implements Comparator<ItemStack>
    {
        @Override
        public int compare(ItemStack i1, ItemStack i2)
        {
            int f = 0, s = 0;
            if(i1.getItem() instanceof ArmorItem)         f = 2;
            else if (i1.getItem() instanceof SwordItem)   f = 3;
            else if (i1.getItem() instanceof AxeItem)     f = 4;
            else if (i1.getItem() instanceof PickaxeItem) f = 5;
            else if (i1.getItem() instanceof ToolItem)    f = 1;
            else if (i1.getItem() instanceof BlockItem)   f = 6;

            if(i2.getItem() instanceof ArmorItem)         s = 2;
            else if (i2.getItem() instanceof SwordItem)   s = 3;
            else if (i2.getItem() instanceof AxeItem)     s = 4;
            else if (i2.getItem() instanceof PickaxeItem) s = 5;
            else if (i2.getItem() instanceof ToolItem)    s = 6;
            else if (i2.getItem() instanceof BlockItem)   s = 7;

            return Integer.compare(f, s);
        }
    }
}

