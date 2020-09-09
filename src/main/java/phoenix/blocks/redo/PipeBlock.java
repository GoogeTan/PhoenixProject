package phoenix.blocks.redo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import phoenix.tile.PipeTile;
import phoenix.utils.BlockWithTile;
import phoenix.utils.IFluidMechanism;
import phoenix.world.FluidGraphSaveData;

import javax.annotation.Nullable;

public class PipeBlock extends BlockWithTile<PipeTile>
{
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    protected static final VoxelShape NORMAL = Block.makeCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);

    public PipeBlock()
    {
        super(Properties.create(Material.WOOD).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(NORTH, Boolean.valueOf(false))
                .with(EAST,  Boolean.valueOf(false))
                .with(SOUTH, Boolean.valueOf(false))
                .with(WEST,  Boolean.valueOf(false))
                .with(UP,    Boolean.valueOf(false))
                .with(DOWN,  Boolean.valueOf(false)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return this.makeConnections(context.getWorld(), context.getPos());
    }

    public BlockState makeConnections(IBlockReader reader, BlockPos pos)
    {
        TileEntity tile0 = reader.getTileEntity(pos.down());
        TileEntity tile1 = reader.getTileEntity(pos.up());
        TileEntity tile2 = reader.getTileEntity(pos.north());
        TileEntity tile3 = reader.getTileEntity(pos.east());
        TileEntity tile4 = reader.getTileEntity(pos.south());
        TileEntity tile5 = reader.getTileEntity(pos.west());
        return this.getDefaultState()
                .with(DOWN,  tile0 instanceof IFluidMechanism)
                .with(UP,    tile1 instanceof IFluidMechanism)
                .with(NORTH, tile2 instanceof IFluidMechanism)
                .with(EAST,  tile3 instanceof IFluidMechanism)
                .with(SOUTH, tile4 instanceof IFluidMechanism)
                .with(WEST,  tile5 instanceof IFluidMechanism);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) { builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN); }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        worldIn.setBlockState(pos, makeConnections(worldIn, pos));
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public Class getTileEntityClass()
    {
        return PipeTile.class;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new PipeTile();
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos)
    {
        return false;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return NORMAL;
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        FluidGraphSaveData.get((ServerWorld) worldIn).addBlock(worldIn, pos, false);
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
