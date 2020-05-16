package projectend.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import projectend.blocks.title.BlockTileEntity;
import projectend.blocks.title.Updator01TitleEntity;
import projectend.ProjectEnd;

import javax.annotation.Nullable;
public class Updater extends BlockTileEntity
{
    public Updater()
    {
        super("updator", Material.BARRIER, 10, 10, SoundType.GLASS);
        this.setCreativeTab(ProjectEnd.TheEndOfCreativeTabs);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    @Override
    public Class getTileEntityClass()
    {
        return Updator01TitleEntity.class;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState)
    {
        return new Updator01TitleEntity();
    }
}
