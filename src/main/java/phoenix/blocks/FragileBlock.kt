package phoenix.blocks

import com.google.common.collect.ImmutableList
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.state.EnumProperty
import net.minecraft.state.IProperty
import net.minecraft.state.Property
import net.minecraft.state.StateContainer
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader

object FragileBlock : Block(Properties.create(Material.ROCK))
{
    val STATE: EnumProperty<FragileBlockState> = EnumProperty.create("state", FragileBlockState::class.java, ImmutableList.copyOf(FragileBlockState.values()))

    fun init()
    {
        var st = stateContainer.baseState.with(STATE, FragileBlockState.END_STONE)
        for(i in FragileBlockState.values())
        {
            for(j : IProperty<*> in i.block.defaultState.properties)
            {
                st = blockstate(st, j, j.allowedValues.iterator().next())
            }
        }
    }

    fun <T : Comparable<T>> blockstate(state: BlockState, prop : IProperty<T>, t : T) = state.with(prop, t)

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        for (i in FragileBlockState.values())
        {
             i.fillStateContainer(builder)
        }
        super.fillStateContainer(builder)
    }

    override fun getLightValue(state: BlockState) = state[STATE].getLightValue(state)

    override fun isFlammable(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction) = state[STATE].isFlammable(state, world, pos, face);
}