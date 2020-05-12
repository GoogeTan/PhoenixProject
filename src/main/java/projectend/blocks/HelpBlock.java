package projectend.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Random;

public class HelpBlock extends Block
{
    public static final PropertyInteger TYPE = PropertyInteger.create("type", 0 , 15);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.1D, 0.1D, 0.1D, 0.9D, 0.4D, 0.9D);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.1D, 0.4D, 0.1D, 0.9D, 0.9D, 0.9D);

    public HelpBlock()
    {
        super(Material.ROCK);
        setRegistryName("help");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, 0));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {TYPE});
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        if(state.equals(this.blockState.getBaseState().withProperty(TYPE, 0)))
        return Item.getItemFromBlock(Blocks.END_BRICKS);
        if(state.equals(this.blockState.getBaseState().withProperty(TYPE, 1)))
        return Item.getItemFromBlock(Blocks.PURPUR_SLAB);
        if(state.equals(this.blockState.getBaseState().withProperty(TYPE, 2)))
        return Item.getItemFromBlock(Blocks.PURPUR_SLAB);

        return null;
    }
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if(state.equals(this.blockState.getBaseState().withProperty(TYPE, 1)))
        return AABB_TOP_HALF;
        if(state.equals(this.blockState.getBaseState().withProperty(TYPE, 2)))
        return AABB_BOTTOM_HALF;

        return FULL_BLOCK_AABB;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }
}
