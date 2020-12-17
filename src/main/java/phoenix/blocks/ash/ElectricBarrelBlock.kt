package phoenix.blocks.ash

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.CompoundNBT
import net.minecraft.state.IntegerProperty
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.state.properties.BlockStateProperties.POWER_0_15
import net.minecraft.tileentity.PistonTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.IBooleanFunction
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.math.shapes.VoxelShapes
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.IBlockReader
import net.minecraft.world.ILightReader
import net.minecraft.world.World
import net.minecraft.world.server.ServerWorld
import phoenix.init.PhoenixItems
import phoenix.tile.ash.PotteryBarrelTile
import phoenix.utils.LogManager
import phoenix.utils.block.BlockWithTile
import phoenix.utils.block.IColoredBlock
import javax.annotation.Nonnull
import javax.annotation.ParametersAreNonnullByDefault
import kotlin.math.sqrt

class ElectricBarrelBlock : BlockWithTile(Properties.create(Material.BAMBOO)), IColoredBlock
{
    @Nonnull
    @ParametersAreNonnullByDefault
    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = SHAPE

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(state)
    }

    override fun neighborChanged(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        blockIn: Block?,
        fromPos: BlockPos?,
        isMoving: Boolean
    )
    {
        if (!worldIn.isRemote)
        {
            if(worldIn.getBlockState(fromPos)[POWER_0_15] > 0)
            {
                try
                {
                    (worldIn.getTileEntity(pos) as PotteryBarrelTile).incrementJumpsCount()
                } catch (e: Exception)
                {
                    LogManager.error(this, "Can not increment jump count at $pos")
                }

            }
        }
    }

    override fun canConnectRedstone(state: BlockState?, world: IBlockReader?, pos: BlockPos?, side: Direction?) = true

    override fun onFallenUpon(worldIn: World, @Nonnull pos: BlockPos, entityIn: Entity, fallDistance: Float)
    {
        val state = worldIn.getBlockState(pos)
        if (pos.y < entityIn.posY && state.get(Companion.state) == 2 && worldIn.getTileEntity(pos) != null)
        {
            (worldIn.getTileEntity(pos) as PotteryBarrelTile?)!!.incrementJumpsCount()
            if (!worldIn.isRemote) entityIn.sendMessage(StringTextComponent((worldIn.getTileEntity(pos) as PotteryBarrelTile?)!!.jumpsCount.toString() + " "))
        }
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance)
    }

    private fun setState(worldIn: World, pos: BlockPos?, state: BlockState, level: Int)
    {
        worldIn.setBlockState(pos, state.with(Companion.state, level))
    }

    override fun hasComparatorInputOverride(state: BlockState): Boolean = true

    @ParametersAreNonnullByDefault
    override fun getComparatorInputOverride(blockState: BlockState, worldIn: World, pos: BlockPos): Int
    {
        var countOfJumps = 0
        try
        {
            countOfJumps = (worldIn.getTileEntity(pos) as PotteryBarrelTile?)!!.jumpsCount
        } catch (ignored: Exception)
        {
        }
        return (sqrt(countOfJumps.toDouble()) / sqrt(1000.0) * 15).toInt()
    }

    override fun createTileEntity(state: BlockState, world: IBlockReader) = PotteryBarrelTile()

    override fun getBlockColor() = IBlockColor { _: BlockState, _: ILightReader?, _: BlockPos?, _: Int -> Material.WATER.color.colorValue }
    override fun getItemColor() = IItemColor { _, _ -> Material.WATER.color.colorValue }

    companion object
    {
        val SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0.0, 0.0, 4.0, 16.0, 3.0, 12.0), makeCuboidShape(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), makeCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), makeCuboidShape(2.0, 4.0, 2.0, 14.0, 16.0, 14.0)), IBooleanFunction.ONLY_FIRST)
        val state = IntegerProperty.create("state", 0, 2)
    }

    init
    {
        defaultState = stateContainer.baseState.with(state, Integer.valueOf(0))
    }
}