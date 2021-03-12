package phoenix.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.state.StateContainer
import net.minecraft.util.IStringSerializable

enum class FragileBlockState(var nameIn: String, var block: Block) : IStringSerializable
{
    END_STONE("end_stone", Blocks.END_STONE);

    override fun getName(): String = nameIn

    fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
    }

    fun applyStates(state: BlockState)
    {
    }
}