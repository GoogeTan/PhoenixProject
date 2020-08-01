package phoenix;

import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import org.apache.logging.log4j.util.PropertySource;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.function.Supplier;

public class PhoenixGroup extends ItemGroup
{

    @Nonnull
    private final Supplier<ItemStack> iconSupplier;

    public PhoenixGroup(@Nonnull final String name, @Nonnull final Supplier<ItemStack> iconSupplier)
    {
        super(name);
        this.iconSupplier = iconSupplier;
    }

    @Override
    @Nonnull
    public ItemStack createIcon() {
        return iconSupplier.get();
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

    class comp implements Comparator<ItemStack>
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

            if(f > s) return 1;
            else if (s > f) return -1;
            else return 0;
        }
    }
}

