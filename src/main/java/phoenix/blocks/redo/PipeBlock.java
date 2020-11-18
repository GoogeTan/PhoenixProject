package phoenix.blocks.redo;

import mezz.jei.api.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import phoenix.tile.redo.PipeTile;
import phoenix.utils.block.BlockWithTile;
import phoenix.utils.pipe.FluidGraphSaveData;
import phoenix.utils.pipe.IFluidPipe;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.state.properties.BlockStateProperties.WATERLOGGED;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PipeBlock extends BlockWithTile<PipeTile> implements IWaterLoggable
{
    public static final BooleanProperty UP    = BooleanProperty.create("up");
    public static final BooleanProperty DOWN  = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST  = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST  = BooleanProperty.create("west");

    protected static final VoxelShape NORMAL  = Block.makeCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);

    public PipeBlock()
    {
        super(Properties.create(Material.WOOD).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(NORTH, Boolean.valueOf(false))
                .with(EAST,  Boolean.valueOf(false))
                .with(SOUTH, Boolean.valueOf(false))
                .with(WEST,  Boolean.valueOf(false))
                .with(UP,    Boolean.valueOf(false))
                .with(DOWN,  Boolean.valueOf(false))
                .with(WATERLOGGED, Boolean.FALSE));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
    {
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
        return this.makeConnections(context.getWorld(), context.getPos()).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
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
                .with(DOWN,  tile0 instanceof IFluidPipe)
                .with(UP,    tile1 instanceof IFluidPipe)
                .with(NORTH, tile2 instanceof IFluidPipe)
                .with(EAST,  tile3 instanceof IFluidPipe)
                .with(SOUTH, tile4 instanceof IFluidPipe)
                .with(WEST,  tile5 instanceof IFluidPipe);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) { builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN, WATERLOGGED); }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving)
    {
        IFluidState ifluidstate = worldIn.getFluidState(pos);
        worldIn.setBlockState(pos, makeConnections(worldIn, pos).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER));
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.INVISIBLE;
    }

      
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

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
    {
        if(!worldIn.isRemote)
        {
            FluidGraphSaveData.get((ServerWorld) worldIn).addBlock((ServerWorld) worldIn, pos, false, false);
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }
}
