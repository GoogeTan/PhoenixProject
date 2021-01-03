package phoenix.blocks.redo

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.state.IntegerProperty
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties.POWER_0_15
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import phoenix.tile.ash.PotteryBarrelTile
import phoenix.utils.LogManager
import phoenix.utils.block.BlockWithTile
import javax.annotation.ParametersAreNonnullByDefault

class ElectricBarrelBlock : BlockWithTile(Properties.create(Material.BAMBOO))
{
    companion object
    {
        val STATE = IntegerProperty.create("state", 0, 2)
    }
    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(STATE)
    }

    override fun neighborChanged(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        blockIn: Block,
        fromPos: BlockPos,
        isMoving: Boolean
    )
    {
        if (!worldIn.isRemote)
        {
            if(worldIn.getBlockState(fromPos).has(POWER_0_15))
            {
                if (worldIn.getStrongPower(fromPos) > 0 && worldIn.getBlockState(pos)[STATE] == 2)
                {
                    try
                    {
                        (worldIn.getTileEntity(pos) as PotteryBarrelTile).incrementJumpsCount()
                    } catch (e: Exception)
                    {
                        LogManager.log(this, "Can not increment jump count at $pos")
                    }

                }
            }
        }
    }

    override fun canConnectRedstone(state: BlockState?, world: IBlockReader?, pos: BlockPos?, side: Direction?) = true

    override fun hasComparatorInputOverride(state: BlockState): Boolean = true

    @ParametersAreNonnullByDefault
    override fun getComparatorInputOverride(blockState: BlockState, worldIn: World, pos: BlockPos): Int
    {
        var countOfJumps = 0
        try
        {
            countOfJumps = (worldIn.getTileEntity(pos) as PotteryBarrelTile).jumpsCount
        } catch (ignored: Exception) { }

        return 15.coerceAtMost((countOfJumps / 40.0 * 15).toInt())
    }

    override fun createTileEntity(state: BlockState, world: IBlockReader) = PotteryBarrelTile()

    init
    {
        defaultState = stateContainer.baseState.with(STATE, Integer.valueOf(0))
    }
}