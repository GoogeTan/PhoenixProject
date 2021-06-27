package phoenix.blocks.ash

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluids
import net.minecraft.inventory.InventoryHelper
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.state.IntegerProperty
import net.minecraft.state.StateContainer
import net.minecraft.tags.FluidTags
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
import net.minecraft.world.IBlockReader
import net.minecraft.world.ILightReader
import net.minecraft.world.World
import net.minecraft.world.biome.BiomeColors
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.fluids.FluidAttributes
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.items.ItemHandlerHelper
import phoenix.init.PhxItems
import phoenix.tile.ash.PotteryBarrelTile
import phoenix.utils.block.IColoredBlock
import java.util.*
import javax.annotation.Nonnull
import javax.annotation.ParametersAreNonnullByDefault
import kotlin.math.min


class PotteryBarrelBlock : Block(Properties.create(Material.BAMBOO).hardnessAndResistance(4.0f)), IColoredBlock
{
    @Nonnull
    @ParametersAreNonnullByDefault
    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext): VoxelShape = SHAPE

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(POTTERY_STATE)
    }

    override fun onFallenUpon(worldIn: World, @Nonnull pos: BlockPos, entityIn: Entity, fallDistance: Float)
    {
        val state = worldIn.getBlockState(pos)
        if (!worldIn.isRemote && pos.y < entityIn.posY && state.get(Companion.POTTERY_STATE) == 3 && worldIn.rand.nextDouble() < 0.05)
        {
            setState(worldIn, pos, state, 0)
            spawnAsEntity(worldIn, pos, ItemStack(PhxItems.HIGH_QUALITY_CLAY_ITEM))
        }
        super.onFallenUpon(worldIn, pos, entityIn, fallDistance)
    }

    @Nonnull
    @ParametersAreNonnullByDefault
    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, player: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult): ActionResultType
    {
        val stack = player.getHeldItem(handIn)
        val potteryState = state.get(Companion.POTTERY_STATE)
        when (potteryState)
        {
            0 -> // Empty
            {
                if (insertWater(stack, player, handIn))
                {
                    if (!worldIn.isRemote)
                        setState(worldIn, pos, state, 1);
                    return ActionResultType.SUCCESS;
                } else if (stack.item == Items.CLAY)
                {
                    if (!worldIn.isRemote)
                    {
                        stack.shrink(1)
                        setState(worldIn, pos, state, 2);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
            1 -> // Water
            {
                if (extractWater(stack, player, handIn))
                {
                    if (!worldIn.isRemote)
                        setState(worldIn, pos, state, 0);
                    return ActionResultType.SUCCESS;
                } else if (stack.item == Items.CLAY)
                {
                    if (!worldIn.isRemote)
                    {
                        stack.shrink(1)
                        setState(worldIn, pos, state, 3);
                    }
                    return ActionResultType.SUCCESS;
                }
            }
            2 -> // Clay
            {
                if (insertWater(stack, player, handIn) && !worldIn.isRemote)
                {
                    setState(worldIn, pos, state, 3);
                } else if (!worldIn.isRemote)
                {
                    ItemHandlerHelper.giveItemToPlayer(player, ItemStack(Items.CLAY))
                    setState(worldIn, pos, state, 0);
                }
                return ActionResultType.SUCCESS;
            }
            3 -> // Water and clay
            {
                if (extractWater(stack, player, handIn))
                {
                    if (!worldIn.isRemote)
                        setState(worldIn, pos, state, 2);
                } else if (!worldIn.isRemote)
                {
                    ItemHandlerHelper.giveItemToPlayer(player, ItemStack(Items.CLAY))
                    setState(worldIn, pos, state, 1);
                }
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS
    }

    private fun insertWater(stack : ItemStack, player : PlayerEntity, hand : Hand): Boolean
    {
        var drained = false
        FluidUtil.getFluidHandler(stack).ifPresent{ t ->
            val fluidStack = t.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE)
            if (fluidStack.amount >= FluidAttributes.BUCKET_VOLUME && fluidStack.fluid.isIn(FluidTags.WATER))
            {
                if (!player.isCreative)
                {
                    t.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE)
                    player.setHeldItem(hand, t.container)
                }
                drained = true;
            }
        }
        return drained
    }

    private fun extractWater(stack : ItemStack, player : PlayerEntity, hand : Hand): Boolean
    {
        var filled = false
        FluidUtil.getFluidHandler(stack).ifPresent{ t ->
            val amount = t.fill(FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.SIMULATE)
            if (amount >= FluidAttributes.BUCKET_VOLUME)
            {
                t.fill(FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE)
                player.setHeldItem(hand, t.container)
                filled = true;
            }
        }
        return filled
    }

    private fun setState(worldIn: World, pos: BlockPos?, state: BlockState, level: Int)
    {
        worldIn.setBlockState(pos, state.with(Companion.POTTERY_STATE, level))
    }

    override fun hasComparatorInputOverride(state: BlockState): Boolean = true

    @ParametersAreNonnullByDefault
    override fun getComparatorInputOverride(blockState: BlockState, worldIn: World, pos: BlockPos): Int
    {
        val potteryState = blockState.get(POTTERY_STATE)
        return if (potteryState == 0) 0 else if (potteryState == 1 || potteryState == 2) 5 else 10
    }

    override fun getBlockColor() = IBlockColor { _: BlockState, world: ILightReader?, pos: BlockPos?, _: Int -> if (world != null && pos != null) BiomeColors.getWaterColor(world, pos) else -1 }
    override fun getItemColor() = IItemColor { _: ItemStack, _: Int -> Material.WATER.color.colorValue }
    override fun getDrops(state: BlockState, builder: LootContext.Builder) = listOf(ItemStack(this))

    companion object
    {
        val SHAPE = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), VoxelShapes.or(makeCuboidShape(0.0, 0.0, 4.0, 16.0, 3.0, 12.0), makeCuboidShape(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), makeCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), makeCuboidShape(2.0, 4.0, 2.0, 14.0, 16.0, 14.0)), IBooleanFunction.ONLY_FIRST)
        val POTTERY_STATE: IntegerProperty = IntegerProperty.create("pottery_state", 0, 3)
    }

    init
    {
        defaultState = stateContainer.baseState.with(POTTERY_STATE, Integer.valueOf(0))
    }
}