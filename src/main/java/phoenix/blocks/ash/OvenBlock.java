package phoenix.blocks.ash;

import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import phoenix.tile.ash.OvenTile;

import javax.annotation.Nullable;

public class OvenBlock extends ContainerBlock
{
    public OvenBlock()
    {
        super(Properties.create(Material.ROCK));
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new OvenTile();
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new OvenTile();
    }
}
