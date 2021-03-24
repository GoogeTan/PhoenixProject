package phoenix.blocks.redo

import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.IWaterLoggable
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.fluid.IFluidState
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.ItemGroup
import net.minecraft.state.BooleanProperty
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.fml.client.registry.IRenderFactory
import phoenix.Phoenix.Companion.REDO
import phoenix.tile.IFluidThing
import phoenix.tile.redo.PipeTile
import phoenix.utils.block.BlockWithTile
import phoenix.utils.block.ICustomGroup

class PipeBlock : BlockWithTile(Properties.create(Material.WOOD).notSolid().hardnessAndResistance(1.0f)), IWaterLoggable, ICustomGroup
{
    override fun onBlockActivated(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        player: PlayerEntity,
        handIn: Hand,
        hit: BlockRayTraceResult
    ): ActionResultType
    {
        if (worldIn.isRemote)
        {
            val pipeTile = worldIn.getTileEntity(pos) as PipeTile?
            player.sendMessage(StringTextComponent(pipeTile!!.getPercent().toString() + " " + pipeTile.tank.fluid.amount))
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit)
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState
    {
        return makeConnections(context.world, context.pos).with(BlockStateProperties.WATERLOGGED, context.world.getFluidState(context.pos).fluid === Fluids.WATER)
    }

    fun makeConnections(reader: IBlockReader, pos: BlockPos): BlockState
    {
        return defaultState
            .with(DOWN,  reader.getTileEntity(pos.down())  is IFluidThing)
            .with(UP,    reader.getTileEntity(pos.up())    is IFluidThing)
            .with(NORTH, reader.getTileEntity(pos.north()) is IFluidThing)
            .with(EAST,  reader.getTileEntity(pos.east())  is IFluidThing)
            .with(SOUTH, reader.getTileEntity(pos.south()) is IFluidThing)
            .with(WEST,  reader.getTileEntity(pos.west())  is IFluidThing)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(NORTH, EAST, WEST, SOUTH, UP, DOWN, BlockStateProperties.WATERLOGGED)
    }

    override fun neighborChanged(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        blockIn: Block,
        fromPos: BlockPos,
        isMoving: Boolean
    )
    {
        worldIn.setBlockState(pos, makeConnections(worldIn, pos).with(BlockStateProperties.WATERLOGGED, worldIn.getFluidState(pos).fluid === Fluids.WATER))
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving)
    }

    override fun getFluidState(state: BlockState): IFluidState = if (state.get(BlockStateProperties.WATERLOGGED)) Fluids.WATER.getStillFluidState(false) else super.getFluidState(state)

    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.INVISIBLE

    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = PipeTile()

    override fun isNormalCube(state: BlockState, worldIn: IBlockReader, pos: BlockPos): Boolean = false

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = NORMAL

    override val tab: ItemGroup
        get() = REDO

    companion object
    {
        val UP = BooleanProperty.create("up")
        val DOWN = BooleanProperty.create("down")
        val NORTH = BooleanProperty.create("north")
        val EAST = BooleanProperty.create("east")
        val SOUTH = BooleanProperty.create("south")
        val WEST = BooleanProperty.create("west")
        private val NORMAL = makeCuboidShape(4.0, 4.0, 4.0, 12.0, 12.0, 12.0)
    }

    init
    {
        defaultState = stateContainer.baseState
            .with(NORTH, false)
            .with(EAST, false)
            .with(SOUTH, false)
            .with(WEST, false)
            .with(UP, false)
            .with(DOWN, false)
            .with(BlockStateProperties.WATERLOGGED, false)
    }
}
