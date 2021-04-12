package phoenix.blocks.redo

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.IGrowable
import net.minecraft.block.material.Material
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties.AGE_0_3
import net.minecraft.state.properties.BlockStateProperties.HORIZONTAL_FACING
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraft.world.storage.loot.LootContext
import phoenix.Phoenix
import phoenix.init.PhoenixBlocks
import phoenix.utils.block.ICustomGroup
import java.util.*

object SetaBlock : Block(Properties.create(Material.CACTUS).notSolid().tickRandomly().lightValue(5).hardnessAndResistance(1.0f)), IGrowable, ICustomGroup
{
    private val SHAPE: VoxelShape = makeCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0)

    init
    {
        defaultState = this.stateContainer.baseState
                .with(AGE_0_3, 0)
                .with(HORIZONTAL_FACING, Direction.NORTH)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(AGE_0_3)
        builder.add(HORIZONTAL_FACING)
        super.fillStateContainer(builder)
    }

    override fun isValidPosition(state: BlockState, worldIn: IWorldReader, pos: BlockPos): Boolean = worldIn.getBlockState(pos.up()).block == PhoenixBlocks.FERTILE_END_STONE

    override fun tick(state: BlockState, worldIn: ServerWorld, pos: BlockPos, rand: Random)
    {
        if(!isValidPosition(state, worldIn, pos))
            worldIn.destroyBlock(pos, true)
        if(worldIn.rand.nextInt(10) == 0)
            grow(worldIn, rand, pos, state)
    }

    override fun canGrow(worldIn: IBlockReader, pos: BlockPos, state: BlockState, isClient: Boolean) = true
    override fun canUseBonemeal(worldIn: World, rand: Random, pos: BlockPos, state: BlockState) = false

    override fun grow(worldIn: ServerWorld, rand: Random, pos: BlockPos, state: BlockState)
    {
        val age = state[AGE_0_3]
        if(age < 3)
            worldIn.setBlockState(pos, state.with(AGE_0_3, age + 1))
        if(age >= 2)
        {
            var pos2 = BlockPos.ZERO
            val current = BlockPos.Mutable(pos)
            for (x in -1..1)
                for (y in -1..1)
                    for (z in -1..1)
                    {
                        current.setPos(pos.x + x, pos.y + y, pos.z + z)
                        if (worldIn.isAirBlock(current) && current != pos && isValidPosition(worldIn.getBlockState(current), worldIn, current))
                            if(pos2 == BlockPos.ZERO || rand.nextBoolean())
                                pos2 = current.toImmutable()
                    }
            if(pos != pos2 && pos2 != BlockPos.ZERO)
            {
                worldIn.setBlockState(pos2, PhoenixBlocks.SETA.defaultState.with(HORIZONTAL_FACING, Direction.Plane.HORIZONTAL.random(rand)), 2)
            }
        }
    }

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = SHAPE

    override val tab = Phoenix.REDO

    override fun getDrops(state: BlockState, builder: LootContext.Builder) = listOf(ItemStack(this, state[AGE_0_3] + 1))
}
