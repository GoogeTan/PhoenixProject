package phoenix.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import phoenix.tile.UpdaterTile;
import phoenix.utils.BlockWithTile;

import javax.annotation.Nullable;

public class UpdaterBlock extends BlockWithTile<UpdaterTile>
{
    public UpdaterBlock()
    {
        super(Block.Properties.create(Material.ROCK).lightValue(5).hardnessAndResistance(-1));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new UpdaterTile();
    }

    @Override
    public Class getTileEntityClass()
    {
        return UpdaterTile.class;
    }

    @Override
    public boolean hasTileEntity()
    {
        return true;
    }
}
