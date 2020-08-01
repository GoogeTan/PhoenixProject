package phoenix.blocks.redo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import phoenix.tile.TankTile;
import phoenix.utils.BlockWithTile;
import phoenix.utils.IFluidMechanism;
import phoenix.world.EndBiomedDimension;

import javax.annotation.Nullable;

public class TankBlock extends BlockWithTile
{
    public TankBlock()
    {
        super(Block.Properties.create(Material.ROCK).lightValue(5).notSolid());
    }

    @Override
    public Class getTileEntityClass()
    {
        return TileEntity.class;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        TankTile tileTank = (TankTile) worldIn.getTileEntity(pos);
        if (player.getHeldItem(handIn).getItem() == Items.BUCKET && tileTank.tank.getFluid().getAmount() >= FluidAttributes.BUCKET_VOLUME)
        {
            FluidStack stack = tileTank.tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            player.getHeldItem(handIn).shrink(1);
            player.addItemStackToInventory(FluidUtil.getFilledBucket(stack));
            return ActionResultType.SUCCESS;
        }
        if(player.getHeldItem(handIn).getItem() instanceof BucketItem && tileTank.tank.getCapacity() - tileTank.tank.getFluidAmount() >= FluidAttributes.BUCKET_VOLUME)
        {
            tileTank.tank.fill(FluidUtil.getFluidContained(player.getHeldItem(handIn)).orElse(FluidStack.EMPTY), IFluidHandler.FluidAction.EXECUTE);
            player.getHeldItem(handIn).shrink(1);
            player.addItemStackToInventory(new ItemStack(Items.BUCKET, 1));
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.CONSUME;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new TankTile();
    }
}
