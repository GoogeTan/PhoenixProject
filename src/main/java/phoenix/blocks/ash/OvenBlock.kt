package phoenix.blocks.ash

import net.minecraft.block.Block
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particles.ParticleTypes
import net.minecraft.state.BooleanProperty
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.ToolType
import phoenix.init.PhoenixBlocks
import phoenix.network.NetworkHandler
import phoenix.network.SyncOvenPacket
import phoenix.recipes.OvenRecipe
import phoenix.tile.ash.OvenTile
import phoenix.utils.SizedArrayList
import phoenix.utils.block.BlockWithTile
import java.util.*

class OvenBlock : BlockWithTile(Properties.create(Material.ROCK).notSolid().hardnessAndResistance(10f).harvestTool(ToolType.PICKAXE))
{
    companion object
    {
        val WORKING : BooleanProperty = BooleanProperty.create("working")
        val SHAPE   : VoxelShape      = Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 32.0, 16.0)
    }

    init
    {
        defaultState = stateContainer.baseState.with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).with(WORKING, false)
    }

    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, playerIn: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult) : ActionResultType
    {
        if (!worldIn.isRemote && !playerIn.isSneaking)
        {
            val tile = worldIn.getTileEntity(pos) as OvenTile
            val stack: ItemStack = playerIn.getHeldItem(handIn)

            val list = tile.outOtherItems()
            for (i in list)
                playerIn.addItemStackToInventory(i)
            if (OvenRecipe.recipes_from_inputs[stack.item] != null)
            {
                if(tile.addItem(stack))
                {
                    playerIn.getHeldItem(handIn).shrink(1)
                }
                NetworkHandler.sendToAll(SyncOvenPacket(tile))
            }
            else if (stack.item != Items.LAVA_BUCKET && ForgeHooks.getBurnTime(stack) > 0)
            {
                tile.burnTime += ForgeHooks.getBurnTime(stack)
                playerIn.getHeldItem(handIn).shrink(1)
                worldIn.setBlockState(pos, worldIn.getBlockState(pos).with(WORKING, true))
            }
            return ActionResultType.SUCCESS
        }

        return ActionResultType.PASS
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(BlockStateProperties.HORIZONTAL_FACING).add(WORKING)
    }

    override fun getStateForPlacement(context: BlockItemUseContext) = defaultState.with(BlockStateProperties.HORIZONTAL_FACING, context.placementHorizontalFacing.opposite)
    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
    override fun createTileEntity(state: BlockState, world: IBlockReader): TileEntity = OvenTile()

    @OnlyIn(Dist.CLIENT)
    override fun animateTick(stateIn: BlockState, worldIn: World, pos: BlockPos, rand: Random)
    {
        val time = (worldIn.getTileEntity(pos) as OvenTile).burnTime
        if (time > 0)
        {
            val d0 = pos.x.toDouble() + 0.5
            val d1 = pos.y.toDouble()
            val d2 = pos.z.toDouble() + 0.5
            if (rand.nextDouble() < 0.1)
            {
                worldIn.playSound(d0, d1, d2, SoundEvents.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0f, 1.0f, false)
            }
            val direction = stateIn[BlockStateProperties.HORIZONTAL_FACING]
            val dir = direction.axis
            val d3 = 0.52
            val d4 = rand.nextDouble() * 0.6 - 0.3
            val d5 = if (dir === Direction.Axis.X) direction.xOffset.toDouble() * 0.52 else d4
            val d6 = rand.nextDouble() * 9.0 / 16.0
            val d7 = if (dir === Direction.Axis.Z) direction.zOffset.toDouble() * 0.52 else d4
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0, 0.0, 0.0)

            for (i in 0..2)
                worldIn.addParticle(ParticleTypes.FLAME, pos.x + rand.nextDouble() * 0.6 + 0.1, pos.y + rand.nextDouble() * 0.6 + 0.1, pos.z + rand.nextDouble() / 0.8 + 0.1, 0.0, 0.005, 0.0)
        }
    }

    override fun getDrops(state: BlockState, builder: LootContext.Builder): List<ItemStack> = SizedArrayList.of(ItemStack(PhoenixBlocks.OVEN.get()))

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext) = SHAPE
    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext) = SHAPE
}