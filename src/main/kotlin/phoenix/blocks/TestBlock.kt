package phoenix.blocks

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import phoenix.api.Register

@Register("test_block")
class TestBlock : Block(Properties.create(Material.BAMBOO_SAPLING))
{
}