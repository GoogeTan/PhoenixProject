package phoenix.blocks.redo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import phoenix.init.PhoenixBlocks;

import javax.annotation.Nullable;
import java.util.Random;

import static phoenix.blocks.redo.KikinStemBlock.makeConnections;

public class KikiNFruitBlock extends Block
{
    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_5;
    public KikiNFruitBlock()
    {
        super(Block.Properties.create(Material.PLANTS, MaterialColor.BLUE_TERRACOTTA).tickRandomly().hardnessAndResistance(0.4F).sound(SoundType.WOOD).notSolid());
    }

    @Override
    public void tick(BlockState blockstate, ServerWorld worldIn, BlockPos pos, Random rand)
    {
        if (!this.isValidPosition(blockstate, worldIn, pos))
        {
            worldIn.destroyBlock(pos, true);
        }
        else
        {
            BlockPos blockpos = pos.up(-1);

            if (worldIn.isAirBlock(blockpos) && blockpos.getY() > 0)
            {
                int i = blockstate.get(AGE);

                if (i < 5 && ForgeHooks.onCropsGrowPre(worldIn, blockpos, blockstate, rand.nextInt(1) == 0))
                {
                    boolean flag = false;
                    boolean hasEndStone = false;
                    BlockState iblockstate = worldIn.getBlockState(pos.up());
                    Block block = iblockstate.getBlock();

                    if (block == PhoenixBlocks.FERTILE_END_STONE.get()) {
                        flag = true;
                    }
                    else if (block == PhoenixBlocks.KIKIN_STEAM.get())
                    {
                        int j = 1;

                        for (int k = 0; k < 4; ++k)
                        {
                            Block block1 = worldIn.getBlockState(pos.up(j + 1)).getBlock();
                            if (block1 != PhoenixBlocks.KIKIN_STEAM.get())
                            {
                                if (block1 == PhoenixBlocks.FERTILE_END_STONE.get())  hasEndStone = true;
                                break;
                            }
                            ++j;
                        }
                        int i1 = 4;
                        if (hasEndStone) {
                            ++i1;
                        }

                        if (j < 2 || rand.nextInt(i1) >= j) {
                            flag = true;
                        }
                    }
                    else if (iblockstate.getMaterial() == Material.AIR)
                    {
                        flag = true;
                    }

                    if (flag && areAllNeighborsEmpty(worldIn, blockpos, null) && worldIn.isAirBlock(pos.up(-2)))
                    {
                        worldIn.setBlockState(pos, makeConnections(worldIn, PhoenixBlocks.KIKIN_STEAM.get().getDefaultState(), pos), 2);
                        this.placeGrownFlower(worldIn, blockpos, i);
                    }
                    else if (i < 4)
                    {
                        int l = rand.nextInt(4);
                        boolean flag2 = false;

                        if (hasEndStone) ++l;

                        for (int j1 = 0; j1 < l; ++j1)
                        {
                            Direction enumfacing = Direction.Plane.HORIZONTAL.random(rand);
                            BlockPos blockpos1 = pos.offset(enumfacing);

                            if (worldIn.isAirBlock(blockpos1) &&
                                    worldIn.isAirBlock(blockpos1.up(-1)) &&
                                    areAllNeighborsEmpty(worldIn, blockpos1, enumfacing.getOpposite()))
                            {
                                this.placeGrownFlower(worldIn, blockpos1, i + 1);
                                flag2 = true;
                            }
                        }

                        if (flag2)
                        {
                            worldIn.setBlockState(pos, makeConnections(worldIn, PhoenixBlocks.KIKIN_STEAM.get().getDefaultState(), pos), 2);
                        }
                        else
                        {
                            this.placeDeadFlower(worldIn, pos);
                        }
                    }
                    else if (i == 4)
                    {
                        this.placeDeadFlower(worldIn, pos);
                    }
                    ForgeHooks.onCropsGrowPost(worldIn, pos, blockstate);
                }
            }
        }
    }

    private void placeGrownFlower(World worldIn, BlockPos pos, int age) {
        worldIn.setBlockState(pos, this.getDefaultState().with(AGE, Integer.valueOf(age)), 2);
        worldIn.playEvent(1033, pos, 0);
    }

    private void placeDeadFlower(World worldIn, BlockPos pos) {
        worldIn.setBlockState(pos, this.getDefaultState().with(AGE, Integer.valueOf(5)), 2);
        worldIn.playEvent(1034, pos, 0);
    }

    private static boolean areAllNeighborsEmpty(IWorldReader worldIn, BlockPos pos, @Nullable Direction excludingSide) {
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            if (direction != excludingSide && !worldIn.isAirBlock(pos.offset(direction))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
    {
        BlockState blockstate = worldIn.getBlockState(pos.up());
        Block block = blockstate.getBlock();
        if (block != PhoenixBlocks.KIKIN_STEAM.get() && block != PhoenixBlocks.FERTILE_END_STONE.get())
        {
            if (!blockstate.isAir(worldIn, pos.up()))
            {
                return false;
            }
            else
            {
                boolean flag = false;

                for(Direction direction : Direction.Plane.HORIZONTAL)
                {
                    BlockState blockstate1 = worldIn.getBlockState(pos.offset(direction));
                    if (blockstate1.getBlock() == PhoenixBlocks.KIKIN_STEAM.get())
                    {
                        if (flag)
                        {
                            return false;
                        }

                        flag = true;
                    } else if (!blockstate1.isAir(worldIn, pos.offset(direction)))
                    {
                        return false;
                    }
                }

                return flag;
            }
        } else {
            return true;
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing != Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos)) {
            worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
        }

        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }
}
