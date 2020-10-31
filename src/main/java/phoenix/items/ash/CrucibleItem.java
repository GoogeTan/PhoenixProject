package phoenix.items.ash;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import phoenix.Phoenix;

import javax.annotation.Nullable;

public class CrucibleItem extends Item
{
    public CrucibleItem()
    {
        super(new Item.Properties().group(Phoenix.PHOENIX).maxStackSize(8));
        this.addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter()
        {
            @Override
            public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity livingEntity)
            {
                return 0;
            }
        });
    }

}
