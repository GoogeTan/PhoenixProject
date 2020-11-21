package phoenix.blocks.redo

import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.material.MaterialColor
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.util.Direction
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IWorld
import net.minecraft.world.IWorldReader
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.ForgeHooks
import phoenix.blocks.redo.KikinStemBlock.Companion.makeConnections
import phoenix.init.PhoenixBlocks
import java.util.*


class KikiNFruitBlock : Block(Properties.create(Material.PLANTS, MaterialColor.BLUE_TERRACOTTA).tickRandomly().hardnessAndResistance(0.4f).sound(SoundType.WOOD).notSolid())
{
    override fun tick(blockstate: BlockState, worldIn: ServerWorld, pos: BlockPos, rand: Random)
    {
        if (!isValidPosition(blockstate, worldIn, pos))
        {
            worldIn.destroyBlock(pos, true)
        }
        else
        {
            val blockpos = pos.up(-1)
            if (worldIn.isAirBlock(blockpos) && blockpos.y > 0)
            {
                val i = blockstate.get(AGE)
                if (i < 5 && ForgeHooks.onCropsGrowPre(worldIn, blockpos, blockstate, rand.nextInt(1) == 0))
                {
                    var flag = false
                    var hasEndStone = false
                    val iblockstate = worldIn.getBlockState(pos.up())
                    val block = iblockstate.block
                    if (block === PhoenixBlocks.FERTILE_END_STONE.get())
                    {
                        flag = true
                    } else if (block === PhoenixBlocks.KIKIN_STEAM.get())
                    {
                        var j = 1
                        for (k in 0..3)
                        {
                            val block1 = worldIn.getBlockState(pos.up(j + 1)).block
                            if (block1 !== PhoenixBlocks.KIKIN_STEAM.get())
                            {
                                if (block1 === PhoenixBlocks.FERTILE_END_STONE.get()) hasEndStone = true
                                break
                            }
                            ++j
                        }
                        var i1 = 4
                        if (hasEndStone)
                        {
                            ++i1
                        }
                        if (j < 2 || rand.nextInt(i1) >= j)
                        {
                            flag = true
                        }
                    } else if (iblockstate.material == Material.AIR)
                    {
                        flag = true
                    }
                    if (flag && areAllNeighborsEmpty(worldIn, blockpos, null) && worldIn.isAirBlock(pos.up(-2)))
                    {
                        worldIn.setBlockState(pos, makeConnections(worldIn, PhoenixBlocks.KIKIN_STEAM.get().getDefaultState(), pos), 2)
                        placeGrownFlower(worldIn, blockpos, i)
                    } else if (i < 4)
                    {
                        var l = rand.nextInt(4)
                        var flag2 = false
                        if (hasEndStone) ++l
                        for (j1 in 0..l - 1)
                        {
                            val enumfacing = Direction.Plane.HORIZONTAL.random(rand)
                            val blockpos1 = pos.offset(enumfacing)
                            if (worldIn.isAirBlock(blockpos1) &&
                                    worldIn.isAirBlock(blockpos1.up(-1)) &&
                                    areAllNeighborsEmpty(worldIn, blockpos1, enumfacing.opposite))
                            {
                                placeGrownFlower(worldIn, blockpos1, i + 1)
                                flag2 = true
                            }
                        }
                        if (flag2)
                        {
                            worldIn.setBlockState(pos, makeConnections(worldIn, PhoenixBlocks.KIKIN_STEAM.get().getDefaultState(), pos), 2)
                        } else
                        {
                            placeDeadFlower(worldIn, pos)
                        }
                    } else if (i == 4)
                    {
                        placeDeadFlower(worldIn, pos)
                    }
                    ForgeHooks.onCropsGrowPost(worldIn, pos, blockstate)
                }
            }
        }
    }

    private fun placeGrownFlower(worldIn: World, pos: BlockPos, age: Int)
    {
        worldIn.setBlockState(pos, defaultState.with(AGE, Integer.valueOf(age)), 2)
        worldIn.playEvent(1033, pos, 0)
    }

    private fun placeDeadFlower(worldIn: World, pos: BlockPos)
    {
        worldIn.setBlockState(pos, defaultState.with(AGE, Integer.valueOf(5)), 2)
        worldIn.playEvent(1034, pos, 0)
    }

    override fun isValidPosition(state: BlockState, worldIn: IWorldReader, pos: BlockPos): Boolean
    {
        val blockstate = worldIn.getBlockState(pos.up())
        val block = blockstate.block
        return if (block !== PhoenixBlocks.KIKIN_STEAM.get() && block !== PhoenixBlocks.FERTILE_END_STONE.get())
        {
            if (!blockstate.isAir(worldIn, pos.up()))
            {
                false
            } else
            {
                var flag = false
                for (direction in Direction.Plane.HORIZONTAL)
                {
                    val blockstate1 = worldIn.getBlockState(pos.offset(direction))
                    if (blockstate1.block === PhoenixBlocks.KIKIN_STEAM.get())
                    {
                        if (flag)
                        {
                            return false
                        }
                        flag = true
                    } else if (!blockstate1.isAir(worldIn, pos.offset(direction)))
                    {
                        return false
                    }
                }
                flag
            }
        } else
        {
            true
        }
    }

    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(AGE)
    }

    override fun updatePostPlacement(stateIn: BlockState, facing: Direction, facingState: BlockState, worldIn: IWorld, currentPos: BlockPos, facingPos: BlockPos): BlockState
    {
        if (facing != Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos))
            worldIn.pendingBlockTicks.scheduleTick(currentPos, this, 1)

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos)
    }

    companion object
    {
        val AGE = BlockStateProperties.AGE_0_5
        private fun areAllNeighborsEmpty(worldIn: IWorldReader, pos: BlockPos, excludingSide: Direction?): Boolean
        {
            for (direction in Direction.Plane.HORIZONTAL)
            {
                if (direction != excludingSide && !worldIn.isAirBlock(pos.offset(direction)))
                {
                    return false
                }
            }
            return true
        }
    }
}
