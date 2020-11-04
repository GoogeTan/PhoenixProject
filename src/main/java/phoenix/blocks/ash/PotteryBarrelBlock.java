package phoenix.blocks.ash;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import phoenix.init.PhoenixItems;
import phoenix.tile.ash.PotteryBarrelTile;
import phoenix.utils.BlockWithTile;
import phoenix.utils.block.IColoredBlock;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class PotteryBarrelBlock extends BlockWithTile<PotteryBarrelTile> implements IColoredBlock
{
    protected static final VoxelShape SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), makeCuboidShape(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), makeCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D)), IBooleanFunction.ONLY_FIRST);
    public static final IntegerProperty state = IntegerProperty.create("state", 0, 2);

    public PotteryBarrelBlock()
    {
        super(Properties.create(Material.BAMBOO));
        this.setDefaultState(this.stateContainer.getBaseState().with(state, Integer.valueOf(0)));
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) { builder.add(state); }


    @Override
    public void onFallenUpon(World worldIn, @Nonnull BlockPos pos, Entity entityIn, float fallDistance)
    {
        BlockState state = worldIn.getBlockState(pos);
        if (pos.getY() < entityIn.getPosY() && state.get(PotteryBarrelBlock.state) == 2 && worldIn.getTileEntity(pos) != null)
        {
            ((PotteryBarrelTile)worldIn.getTileEntity(pos)).incrementJumpsCount();
            if(!worldIn.isRemote)
                entityIn.sendMessage(new StringTextComponent(((PotteryBarrelTile)worldIn.getTileEntity(pos)).jumpsCount + " "));
        }
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        int countOfJumps = 0;
        try {
            countOfJumps = ((PotteryBarrelTile)worldIn.getTileEntity(pos)).jumpsCount;
        } catch (Exception ignored){}

        ItemStack itemstack = player.getHeldItem(handIn);
        if (player.getActiveItemStack().equals(new ItemStack(Items.AIR))) //itemstack.isEmpty() ||
        {
            if(state.get(PotteryBarrelBlock.state) == 2)
                setState(worldIn, pos, state, 3);
            else if (state.get(PotteryBarrelBlock.state) == 3)
                setState(worldIn, pos, state, 2);
            return ActionResultType.SUCCESS;
        }

        if(state.get(PotteryBarrelBlock.state) != 3)//!state.get(isClose))
        {
            int stateInt = state.get(PotteryBarrelBlock.state);
            Item item = itemstack.getItem();
            if (item == Items.WATER_BUCKET)
            {
                if (stateInt == 0 && !worldIn.isRemote)
                {
                    if (!player.abilities.isCreativeMode)
                    {
                        player.setHeldItem(handIn, new ItemStack(Items.BUCKET));
                    }
                    setState(worldIn, pos, state, 1);
                    worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }

                return ActionResultType.SUCCESS;
            }
            else if (item == Items.BUCKET)
            {
                if (stateInt >= 0 && !worldIn.isRemote)
                {
                    itemstack.shrink(1);
                    double quality = Math.sqrt(countOfJumps) / Math.sqrt(1000);
                    if (itemstack.isEmpty())
                    {
                        if (stateInt >= 2)
                        {
                            ItemStack stackToAdd = new ItemStack(PhoenixItems.HIGH_QUALITY_CLAY_ITEM.get());
                            if (stackToAdd.getTag() == null)
                                stackToAdd.setTag(new CompoundNBT());

                            stackToAdd.getTag().putDouble("quality", quality);//тут значение в %. От 0 до 1
                            player.setHeldItem(handIn, stackToAdd);
                        }
                        else
                            player.setHeldItem(handIn, new ItemStack(Items.WATER_BUCKET));
                    }
                    else
                    {
                        if (stateInt >= 2)
                        {
                            ItemStack stackToAdd = new ItemStack(PhoenixItems.HIGH_QUALITY_CLAY_ITEM.get());
                            if (stackToAdd.getTag() == null)
                                stackToAdd.setTag(new CompoundNBT());


                            stackToAdd.getTag().putDouble("quality", quality);//тут значение в %. От 0 до 1
                            if (!player.inventory.addItemStackToInventory(stackToAdd))
                                player.dropItem(stackToAdd, false);
                        }
                        else
                        {
                            if (!player.inventory.addItemStackToInventory(new ItemStack(Items.WATER_BUCKET)))
                                player.dropItem(new ItemStack(Items.WATER_BUCKET), false);
                        }
                    }

                    setState(worldIn, pos, state, 0);
                    worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    try {
                        ((PotteryBarrelTile)worldIn.getTileEntity(pos)).nullifyJumpsCount();
                    } catch (Exception ignored){}
                }
                return ActionResultType.SUCCESS;
            }
            else  if(item == Items.CLAY)
            {
                if (stateInt == 1 && !worldIn.isRemote)
                {
                    if (!player.abilities.isCreativeMode) itemstack.shrink(1);
                        setState(worldIn, pos, state, 2);
                    worldIn.playSound(null, pos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
                return ActionResultType.SUCCESS;
            }
            else
            {
                return ActionResultType.PASS;
            }
        }
        else
        {
            return ActionResultType.PASS;
        }
    }


    public void setState(World worldIn, BlockPos pos, BlockState state, int level) {
        worldIn.setBlockState(pos, state.with(PotteryBarrelBlock.state, level));
    }
    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }


    @ParametersAreNonnullByDefault
    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos)
    {
        int countOfJumps = 0;
        try
        {
            countOfJumps = ((PotteryBarrelTile)worldIn.getTileEntity(pos)).jumpsCount;
        } catch (Exception ignored){}

        return (int) ((Math.sqrt(countOfJumps) / Math.sqrt(1000)) * 15);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PotteryBarrelTile();
    }

    @Override
    public IBlockColor getBlockColor()
    {
        return (state, light, pos, tintIndex) -> Material.WATER.getColor().colorValue;
    }

    @Override
    public IItemColor getItemColor()
    {
        return (stack, tintIndex) -> Material.WATER.getColor().colorValue;
    }
}
