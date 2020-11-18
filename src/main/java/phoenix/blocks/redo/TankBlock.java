package phoenix.blocks.redo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import phoenix.tile.redo.TankTile;
import phoenix.utils.pipe.FluidGraphSaveData;

public class TankBlock extends ContainerBlock
{
    public TankBlock()
    {
        super(Block.Properties.create(Material.ROCK).lightValue(5).notSolid());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        TankTile tileTank = (TankTile) worldIn.getTileEntity(pos);
        if (player.getHeldItem(handIn).getItem() == Items.BUCKET && tileTank.getOutput().getFluid().getAmount() >= FluidAttributes.BUCKET_VOLUME)
        {
            FluidStack stack = tileTank.getOutput().drain(1000, IFluidHandler.FluidAction.EXECUTE);
            player.getHeldItem(handIn).shrink(1);
            player.addItemStackToInventory(FluidUtil.getFilledBucket(stack));
            return ActionResultType.SUCCESS;
        }
        if(player.getHeldItem(handIn).getItem() instanceof BucketItem && tileTank.getInput().getCapacity() - tileTank.getInput().getFluidAmount() >= FluidAttributes.BUCKET_VOLUME)
        {
            tileTank.getInput().fill(FluidUtil.getFluidContained(player.getHeldItem(handIn)).orElse(FluidStack.EMPTY), IFluidHandler.FluidAction.EXECUTE);
            player.getHeldItem(handIn).shrink(1);
            player.addItemStackToInventory(new ItemStack(Items.BUCKET, 1));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.CONSUME;
    }

      
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TankTile();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state,    LivingEntity placer, ItemStack stack)
    {
        if(!worldIn.isRemote)
            FluidGraphSaveData.get((ServerWorld) worldIn).addBlock((ServerWorld) worldIn, pos, true, true);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new TankTile();
    }
}
