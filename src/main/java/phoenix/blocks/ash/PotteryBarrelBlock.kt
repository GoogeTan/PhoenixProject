package phoenix.blocks.ash

import net.minecraft.block.Block
import net.minecraft.block.BlockState
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
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
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
import phoenix.init.PhoenixItems
import phoenix.tile.ash.PotteryBarrelTile
import phoenix.utils.block.BlockWithTile
import phoenix.utils.block.IColoredBlock
import javax.annotation.Nonnull
import javax.annotation.ParametersAreNonnullByDefault
import kotlin.math.sqrt


class PotteryBarrelBlock : BlockWithTile(Properties.create(Material.BAMBOO)), IColoredBlock
{
    @Nonnull
    @ParametersAreNonnullByDefault
    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = SHAPE

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(state)
    }

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

    @Nonnull
    @ParametersAreNonnullByDefault
    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult): ActionResultType
    {
        assert(worldIn.getTileEntity(pos) == null)
        val countOfJumps = (worldIn.getTileEntity(pos) as PotteryBarrelTile?)!!.jumpsCount
        val itemStack = player.getHeldItem(handIn)
        if (player.activeItemStack == ItemStack(Items.AIR))
        {
            if (state.get(Companion.state) == 2) setState(worldIn, pos, state, 3) else if (state.get(Companion.state) == 3) setState(worldIn, pos, state, 2)
            return ActionResultType.SUCCESS
        }
        return if (state.get(Companion.state) != 3) //!state.get(isClose))
        {
            val stateInt = state.get(Companion.state)
            val item = itemStack.item
            when
            {
                item === Items.WATER_BUCKET ->
                {
                    if (stateInt == 0 && !worldIn.isRemote)
                    {
                        if (!player.abilities.isCreativeMode)
                        {
                            player.setHeldItem(handIn, ItemStack(Items.BUCKET))
                        }
                        setState(worldIn, pos, state, 1)
                        worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    }
                    ActionResultType.SUCCESS
                }
                item === Items.BUCKET       ->
                {
                    if (stateInt >= 0 && !worldIn.isRemote)
                    {
                        itemStack.shrink(1)
                        val quality = sqrt(countOfJumps.toDouble()) / sqrt(200.0)
                        if (itemStack.isEmpty)
                        {
                            if (stateInt >= 2)
                            {
                                val stackToAdd = ItemStack(PhoenixItems.HIGH_QUALITY_CLAY_ITEM.get())
                                if (stackToAdd.tag == null) stackToAdd.tag = CompoundNBT()
                                stackToAdd.tag!!.putDouble("quality", quality) //тут значение в %. От 0 до 1
                                player.setHeldItem(handIn, stackToAdd)
                            }
                            else
                            {
                                player.setHeldItem(handIn, ItemStack(Items.WATER_BUCKET))
                            }
                        }
                        else
                        {
                            if (stateInt >= 2)
                            {
                                val stackToAdd = ItemStack(PhoenixItems.HIGH_QUALITY_CLAY_ITEM.get())
                                if (stackToAdd.tag == null) stackToAdd.tag = CompoundNBT()
                                stackToAdd.tag!!.putDouble("quality", quality) //тут значение в %. От 0 до 1
                                if (!player.inventory.addItemStackToInventory(stackToAdd)) player.dropItem(stackToAdd, false)
                            }
                            else
                            {
                                if (!player.inventory.addItemStackToInventory(ItemStack(Items.WATER_BUCKET))) player.dropItem(ItemStack(Items.WATER_BUCKET), false)
                            }
                        }
                        setState(worldIn, pos, state, 0)
                        worldIn.playSound(null, pos, SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f)
                        try
                        {
                            (worldIn.getTileEntity(pos) as PotteryBarrelTile?)!!.nullifyJumpsCount()
                        } catch (ignored: Exception) {}
                    }
                    ActionResultType.SUCCESS
                }
                item === Items.CLAY         ->
                {
                    if (stateInt == 1 && !worldIn.isRemote)
                    {
                        if (!player.abilities.isCreativeMode) itemStack.shrink(1)
                        setState(worldIn, pos, state, 2)
                        worldIn.playSound(null, pos, SoundEvents.BLOCK_SAND_BREAK, SoundCategory.BLOCKS, 1.0f, 1.0f)
                    }
                    ActionResultType.SUCCESS
                }
                else                        ->
                {
                    ActionResultType.PASS
                }
            }
        }
        else
        {
            ActionResultType.PASS
        }
    }

    fun setState(worldIn: World, pos: BlockPos?, state: BlockState, level: Int)
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
    override fun getItemColor() = IItemColor { _: ItemStack, _: Int -> Material.WATER.color.colorValue }

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