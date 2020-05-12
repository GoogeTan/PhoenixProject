package projectend;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EndOfTheTabs extends CreativeTabs
{
    public EndOfTheTabs(int index, String label)
    {
        super(index, label);
        this.createIcon();
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Item.getItemFromBlock(Blocks.END_PORTAL_FRAME));
    }
}
