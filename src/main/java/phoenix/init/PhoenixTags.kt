package phoenix.init

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.Tag
import net.minecraft.util.ResourceLocation

object PhoenixTags
{
    object Blocks
    {
        @JvmStatic val MECHANISMS = tag("mechanisms")
        private fun tag(name: String): Tag<Block>
        {
            return BlockTags.Wrapper(ResourceLocation("forge", name))
        }
    }

    object Items
    {
        @JvmStatic val ZIRCONIUM_TOOL = tag("zirconium")
        private fun tag(name: String): Tag<Item>
        {
            return ItemTags.Wrapper(ResourceLocation("forge", name))
        }
    }
}