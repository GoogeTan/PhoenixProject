package phoenix.blocks.ash;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import phoenix.tile.ash.OvenTile;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class OvenBlock extends ContainerBlock
{
    public static final BooleanProperty[] buckets = new BooleanProperty[4];
    static
    {
        for (int i = 0; i < buckets.length; ++i)
            buckets[i] = BooleanProperty.create("hasbucket" + i);
    }
    public OvenBlock()
    {
        super(Properties.create(Material.ROCK));

        BlockState state = this.stateContainer.getBaseState();
        for (BooleanProperty property: buckets) state.with(property, false);
        this.setDefaultState(state);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        for (BooleanProperty bucket : buckets) builder.add(bucket);
        super.fillStateContainer(builder);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new OvenTile();
    }

    @Nullable
    @ParametersAreNonnullByDefault
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new OvenTile();
    }
}
