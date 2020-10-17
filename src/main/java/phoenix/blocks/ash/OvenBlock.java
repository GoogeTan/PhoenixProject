package phoenix.blocks.ash;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import phoenix.utils.BlockWithTile;

public class OvenBlock extends BlockWithTile
{
    public OvenBlock()
    {
        super(Properties.create(Material.ROCK));
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return null;
    }
}
