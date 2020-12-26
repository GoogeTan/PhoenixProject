package phoenix.blocks.redo

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import phoenix.Phoenix.Companion.REDO
import phoenix.utils.block.ICustomGroup

class FertileEndStoneBlock : Block(Properties.create(Material.ROCK)), ICustomGroup
{
    override fun getTab() = REDO
}