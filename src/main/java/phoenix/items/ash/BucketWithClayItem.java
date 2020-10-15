package phoenix.items.ash;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import phoenix.utils.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

public class BucketWithClayItem extends Item
{
    public BucketWithClayItem()
    {
        super(new Properties().maxStackSize(1));
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn)
    {
        if(stack.getTag() == null)
            stack.setTag(new CompoundNBT());

        stack.getTag().putDouble("quality", 0);
        super.onCreated(stack, worldIn, playerIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
    {
        if(stack.getTag() == null)
        {
            stack.setTag(new CompoundNBT());
            stack.getTag().putDouble("quality", 0);
        }


        if(stack.getTag().contains("quality"))
        {
            double quality = stack.getTag().getDouble("quality");
            tooltip.add(new StringTextComponent(StringUtils.translate("phoenix.quality") + quality * 100 + "%"));
        }
        else
        {
            tooltip.add(new StringTextComponent(StringUtils.translate("phoenix.quality") + 0 + "%"));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
