package phoenix.blocks.redo

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.ContainerBlock
import net.minecraft.block.material.Material
import net.minecraft.state.IntegerProperty
import net.minecraft.state.StateContainer
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import phoenix.tile.ash.PotteryBarrelTile
import phoenix.utils.LogManager
import phoenix.utils.getTileAt

class ElectricBarrelBlock : ContainerBlock(Properties.create(Material.BAMBOO))
{
    companion object
    {
        val STATE : IntegerProperty = IntegerProperty.create("state", 0, 2)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(STATE)
    }

    override fun createNewTileEntity(worldIn: IBlockReader) = PotteryBarrelTile()

    override fun neighborChanged(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        blockIn: Block,
        fromPos: BlockPos,
        isMoving: Boolean
    )
    {
        if (!worldIn.isRemote && worldIn.getStrongPower(fromPos) > 0 && worldIn.getBlockState(pos)[STATE] == 2)
        {
            worldIn.getTileAt<PotteryBarrelTile>(pos)?.incrementJumpsCount()
        }
    }

    override fun canConnectRedstone(state: BlockState?, world: IBlockReader?, pos: BlockPos?, side: Direction?) = true

    override fun hasComparatorInputOverride(state: BlockState): Boolean = true

    override fun getComparatorInputOverride(blockState: BlockState, worldIn: World, pos: BlockPos): Int
    {
        val countOfJumps = worldIn.getTileAt<PotteryBarrelTile>(pos)?.jumpsCount ?: 0
        return 15.coerceAtMost((countOfJumps / 40.0 * 15).toInt())
    }

    override fun createTileEntity(state: BlockState, world: IBlockReader) = PotteryBarrelTile()

    init
    {
        defaultState = stateContainer.baseState.with(STATE, Integer.valueOf(0))
    }
}