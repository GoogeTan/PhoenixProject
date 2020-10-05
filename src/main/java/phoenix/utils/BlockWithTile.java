package phoenix.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class BlockWithTile<T extends TileEntity> extends Block
{
	public BlockWithTile(Block.Properties properties)
	{
		super(properties);
	}

	public abstract Class<T> getTileEntityClass();

	public T getTileEntity(World world, BlockPos position)
	{
		return (T) world.getTileEntity(position);
	}

	@Override public boolean hasTileEntity(BlockState state){ return true;	}

	  
	@Override
	public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
}