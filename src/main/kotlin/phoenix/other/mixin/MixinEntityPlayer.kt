package phoenix.other.mixin

import com.google.common.collect.ImmutableMap
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import phoenix.client.gui.diary.Chapter
import phoenix.init.PhxItems

//TODO: Remove this companion object for MixinEntityPlayer.
object MixinEntityPlayer
{
    val itemToChapter: Map<Item, Chapter> = ImmutableMap.of(
        Items.IRON_INGOT, Chapter.STEEL,
        Items.CLAY, Chapter.CLAY,
        Items.CLAY_BALL, Chapter.CLAY,
        PhxItems.HIGH_QUALITY_CLAY_ITEM, Chapter.OVEN
    )
    val specialChapters = listOf<(ItemStack) -> Chapter?> { stack -> if (stack.isEnchanted) Chapter.KNIFE_ENCHANTING else null }
}