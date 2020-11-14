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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import phoenix.tile.CounterTile;

public class CounterBlock extends Block
{
    public CounterBlock()
    {
        super(Properties.create(Material.ROCK));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos position, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult result)
    {

        if (!world.isRemote)
        {
            CounterTile tileEntity = (CounterTile) world.getTileEntity(position);
            tileEntity.incCount();
            playerEntity.sendMessage(new StringTextComponent("Count: " + tileEntity.getCount()));
        }

        return ActionResultType.SUCCESS;
    }

    @Override public boolean hasTileEntity(BlockState state){ return true;	}
    @Override public TileEntity createTileEntity(BlockState state, IBlockReader world) { return new CounterTile(); }
}
