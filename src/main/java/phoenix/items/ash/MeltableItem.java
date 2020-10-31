package phoenix.items.ash;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.Set;
import java.util.stream.Stream;

public class MeltableItem
{
    private static final Set<MeltableItem> VALUES = new ObjectArraySet<>();
    public static final MeltableItem IRON = register(new MeltableItem(Items.IRON_INGOT));
    public static final MeltableItem GOLD = register(new MeltableItem(Items.GOLD_INGOT));

    private final Item item;

    protected MeltableItem(Item itemIn)
    {
        this.item = itemIn;
    }

    private static MeltableItem register(MeltableItem woodTypeIn)
    {
        VALUES.add(woodTypeIn);
        return woodTypeIn;
    }

    public static Stream<MeltableItem> getValues()
    {
        return VALUES.stream();
    }

    public Item getItem()
    {
        return this.item;
    }
}
