package phoenix.blocks.redo

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.world.storage.loot.LootContext
import phoenix.Phoenix.Companion.REDO
import phoenix.api.block.ICustomGroup

object FertileEndStoneBlock : Block(Properties.create(Material.ROCK).hardnessAndResistance(3.0f)), ICustomGroup
{
    override val tab: ItemGroup
        get() = REDO

    override fun getDrops(state: BlockState, builder: LootContext.Builder): List<ItemStack> = listOf(ItemStack(this, 1))
}