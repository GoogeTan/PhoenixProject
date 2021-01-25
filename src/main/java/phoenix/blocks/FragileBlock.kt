package phoenix.blocks

import com.google.common.collect.ImmutableList
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.state.*
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import kotlin.reflect.jvm.javaMethod

class FragileBlock : Block(Properties.create(Material.ROCK))
{
    companion object
    {
        val STATE: EnumProperty<FragileBlockState> = EnumProperty.create(
            "state",
            FragileBlockState::class.java,
            ImmutableList.copyOf(FragileBlockState.values())
        )
    }
    init
    {
        var st = stateContainer.baseState.with(STATE, FragileBlockState.END_STONE)
        for (i in FragileBlockState.values())
        {
            val props = i.block.defaultState.properties;
            i.applyStates(st)
            props.stream().filter { pr: IProperty<*> -> pr.allowedValues.iterator().next() is Int }    .map { pr: IProperty<*> -> pr as IProperty<Int> }    .forEach { pr: IProperty<Int>     -> st = st.with(pr, 0); }
            props.stream().filter { pr: IProperty<*> -> pr.allowedValues.iterator().next() is Boolean }.map { pr: IProperty<*> -> pr as IProperty<Boolean> }.forEach { pr: IProperty<Boolean> -> st = st.with(pr, false); }
            props.stream().filter { pr: IProperty<*> -> pr is DirectionProperty }                      .map { pr: IProperty<*> -> pr as DirectionProperty } .forEach { pr: DirectionProperty  -> st = st.with(pr, Direction.NORTH) }
            defaultState = st
        }
    }

    fun <T : Comparable<T>> blockstate(state: BlockState, prop : IProperty<T>, t : T) = state.with(prop, t)

    public override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(STATE)
        for (i in FragileBlockState.values())
        {
             i.fillStateContainer(builder)
        }
        super.fillStateContainer(builder)
    }

    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult) = state[STATE].block.onBlockActivated(state, worldIn, pos, player, handIn, hit)

    override fun getLightValue(state: BlockState) = if(state.has(STATE)) state[STATE].block.getLightValue(state) else 0

    override fun isFlammable(state: BlockState, world: IBlockReader, pos: BlockPos, face: Direction) = state[STATE].block.isFlammable(state, world, pos, face)
}