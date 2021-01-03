package phoenix.blocks.ash

import com.google.common.collect.ImmutableList
import net.minecraft.block.BlockState
import net.minecraft.block.OreBlock
import net.minecraft.block.material.Material
import net.minecraft.item.ItemStack
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.common.ToolType

object ZirconiumOreBlock : OreBlock(Properties.create(Material.ROCK).hardnessAndResistance(3f).harvestTool(ToolType.PICKAXE))
{
    override fun getDrops(state: BlockState, builder: LootContext.Builder): ImmutableList<ItemStack> = ImmutableList.of(ItemStack(this))
}