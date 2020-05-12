package projectend.blocks.title;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import projectend.blocks.BaseBlock;

import javax.annotation.Nullable;

public abstract class BlockTileEntity<T extends TileEntity> extends BaseBlock
{
	public BlockTileEntity(String name, Material material, float hardness, float resistanse, SoundType soundType){super(name, material, hardness, resistanse, soundType);}

	public abstract Class<T> getTileEntityClass();

	public T getTileEntity(IBlockAccess world, BlockPos position)
	{
		return (T) world.getTileEntity(position);
	}

	@Override public boolean hasTileEntity(IBlockState blockState)
	{
		return true;
	}
	@Nullable @Override	public abstract T createTileEntity(World world, IBlockState blockState);
}
