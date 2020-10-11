package phoenix.blocks.ash;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import phoenix.Phoenix;
import phoenix.init.PhoenixItems;

public class PotteryBarrelBlock extends Block
{
    protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D)), IBooleanFunction.ONLY_FIRST);
    public static final BooleanProperty hasWater = BooleanProperty.create("haswater");
    public static final BooleanProperty hasClay = BooleanProperty.create("hasclay");
    public static final IntegerProperty countOfJumps = IntegerProperty.create("jumps", 0, 1000);

    public PotteryBarrelBlock()
    {
        super(Properties.create(Material.BAMBOO));
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(hasWater, Boolean.valueOf(false)).with(countOfJumps, Integer.valueOf(0)).with(hasClay, Boolean.valueOf(false)));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(hasWater).add(countOfJumps).add(hasClay);
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        BlockState state = worldIn.getBlockState(pos);
        if (pos.getY() < entityIn.getPosY() && state.get(hasClay) && state.get(hasWater))
        {
            worldIn.setBlockState(pos, state.with(countOfJumps, state.get(countOfJumps) + 1));
            try
            {
                entityIn.sendMessage(new StringTextComponent(state.get(countOfJumps) + " "));
            } catch (Exception ignored)
            {
            }
        }
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        ItemStack itemstack = player.getHeldItem(handIn);
        if (itemstack.isEmpty())
        {
            return ActionResultType.PASS;
        }
        else
        {
            boolean  hasWaterInState = state.get(hasWater);
            boolean  hasClayInState = state.get(hasClay);
            Item item = itemstack.getItem();
            if (item == Items.WATER_BUCKET)
            {
                if (!hasWaterInState && !worldIn.isRemote)
                {
                    if (!player.abilities.isCreativeMode)
                    {
                        player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                    }
                    setHasWater(worldIn, pos, state, true);
                    worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.SUCCESS;
            }
            else if (item == Items.BUCKET)
            {
                if (hasWaterInState && !worldIn.isRemote)
                {
                    if (!player.abilities.isCreativeMode)
                    {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty())
                        {
                            if(hasClayInState)
                            {
                                ItemStack stackToAdd = new ItemStack(PhoenixItems.bucket_with_clay.get());
                                if(stackToAdd.getTag() == null)
                                    stackToAdd.setTag(new CompoundNBT());

                                stackToAdd.getTag().putDouble("quality", (Math.sqrt(state.get(countOfJumps)) / Math.sqrt(1000)));//тут значение в %. От 0 до 1
                                player.setHeldItem(handIn, stackToAdd);
                            }
                            else
                                player.setHeldItem(handIn, new ItemStack(Items.WATER_BUCKET));
                        }
                        else
                        {
                            if(hasClayInState)
                            {
                                ItemStack stackToAdd = new ItemStack(PhoenixItems.bucket_with_clay.get());
                                if(stackToAdd.getTag() == null)
                                    stackToAdd.setTag(new CompoundNBT());

                                stackToAdd.getTag().putDouble("quality", (Math.sqrt(state.get(countOfJumps)) / Math.sqrt(1000)));//тут значение в %. От 0 до 1
                                if(!player.inventory.addItemStackToInventory(stackToAdd))
                                    player.dropItem(stackToAdd,false);
                            }
                            else
                            {
                                if(!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET)))
                                    player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                            }
                        }
                    }
                    setHasWater(worldIn, pos, state, false);
                    setHasClay (worldIn, pos, state, false);
                    worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.SUCCESS;
            }
            else  if(item == Items.CLAY)
            {
                if (hasWaterInState && !hasClayInState && !worldIn.isRemote)
                {
                    if (!player.abilities.isCreativeMode) itemstack.shrink(1);
                    setHasClay(worldIn, pos, state, false);
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.SUCCESS;
            }
            else
            {
                return ActionResultType.PASS;
            }
        }
    }


    public void setHasWater(World worldIn, BlockPos pos, BlockState state, boolean level) {
        worldIn.setBlockState(pos, state.with(hasWater, Boolean.valueOf(level)));
    }
    public void setHasClay(World worldIn, BlockPos pos, BlockState state, boolean level) {
        worldIn.setBlockState(pos, state.with(hasClay, Boolean.valueOf(level)));
    }
    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
    {
        return (int) ((blockState.get(countOfJumps) / 1000D) * 15);
    }
}
