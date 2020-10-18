package phoenix.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import phoenix.enity.TalpaEntity;
import phoenix.tile.UpdaterTile;
import phoenix.utils.BlockWithTile;

public class UpdaterBlock extends BlockWithTile<UpdaterTile>
{
    public UpdaterBlock()
    {
        super(Block.Properties.create(Material.ROCK).lightValue(5).hardnessAndResistance(-1));
    }
    
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new UpdaterTile();
    }
}
