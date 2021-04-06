package phoenix.blocks.redo

import net.minecraft.block.BlockState
import net.minecraft.block.RotatedPillarBlock
import net.minecraft.block.material.Material
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.item.ItemEntity
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import phoenix.Phoenix
import phoenix.init.PhoenixBlocks
import phoenix.utils.block.ICustomGroup
import phoenix.utils.getEnchantmentLevel

object WetLogBlock : RotatedPillarBlock(Properties.create(Material.WOOD).hardnessAndResistance(3.0f)), ICustomGroup
{
    override fun spawnAdditionalDrops(state: BlockState, worldIn: World, pos: BlockPos, stack: ItemStack)
    {
        if(stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) != 0)
            worldIn.addEntity(ItemEntity(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ItemStack(PhoenixBlocks.WET_LOG)))
        else
            worldIn.addEntity(ItemEntity(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), ItemStack(PhoenixBlocks.DIED_WET_LOG)))
        super.spawnAdditionalDrops(state, worldIn, pos, stack)
    }

    override val tab: ItemGroup = Phoenix.REDO
}