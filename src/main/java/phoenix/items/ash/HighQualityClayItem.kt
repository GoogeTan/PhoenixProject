package phoenix.items.ash

import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.World
import phoenix.Phoenix
import phoenix.utils.StringUtils


class HighQualityClayItem : Item(Properties().maxStackSize(1).group(Phoenix.ASH))
{
    override fun onCreated(stack: ItemStack, worldIn: World, playerIn: PlayerEntity)
    {
        if (stack.tag == null) stack.tag = CompoundNBT()
        stack.tag!!.putDouble("quality", 0.0)
        super.onCreated(stack, worldIn, playerIn)
    }

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<ITextComponent>, flagIn: ITooltipFlag)
    {
        if (stack.tag == null)
        {
            stack.tag = CompoundNBT()
            stack.tag!!.putDouble("quality", 0.0)
        }
        if (stack.tag!!.contains("quality"))
        {
            val quality = stack.tag!!.getDouble("quality")
            tooltip.add(StringTextComponent(StringUtils.translate("phoenix.quality") + quality * 100 + "%"))
        } else
        {
            tooltip.add(StringTextComponent(StringUtils.translate("phoenix.quality") + 0 + "%"))
        }
        super.addInformation(stack, worldIn, tooltip, flagIn)
    }
}
