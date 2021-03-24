package phoenix.init

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.Tag
import net.minecraft.util.ResourceLocation
import phoenix.Phoenix

object PhoenixTags
{
    object Blocks
    {
        val MECHANISMS = tag("mechanisms")
        private fun tag(name: String): Tag<Block> = BlockTags.Wrapper(ResourceLocation(Phoenix.MOD_ID, name))
    }

    object Items
    {
        val ZIRCONIUM_TOOL = tag("zirconium")

        private fun tag(name: String): Tag<Item> = ItemTags.Wrapper(ResourceLocation(Phoenix.MOD_ID, name))
    }
}