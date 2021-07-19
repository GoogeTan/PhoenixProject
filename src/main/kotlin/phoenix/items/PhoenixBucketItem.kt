package phoenix.items

import net.minecraft.fluid.Fluid
import net.minecraft.item.BucketItem
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraftforge.common.capabilities.ICapabilityProvider
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper

class PhoenixBucketItem(fluid : () -> Fluid) : BucketItem(fluid, Properties().containerItem(Items.BUCKET).maxStackSize(1).group(
    ItemGroup.MATERIALS))
{
    override fun initCapabilities(stack: ItemStack, nbt: CompoundNBT?): ICapabilityProvider = FluidBucketWrapper(stack)
}