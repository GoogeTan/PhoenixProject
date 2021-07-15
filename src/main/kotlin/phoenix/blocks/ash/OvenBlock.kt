package phoenix.blocks.ash

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItemUseContext
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particles.ParticleTypes
import net.minecraft.state.IntegerProperty
import net.minecraft.state.StateContainer
import net.minecraft.state.properties.BlockStateProperties
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.shapes.ISelectionContext
import net.minecraft.util.math.shapes.VoxelShape
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.Explosion
import net.minecraft.world.IBlockReader
import net.minecraft.world.IWorld
import net.minecraft.world.World
import net.minecraft.world.storage.loot.LootContext
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.ToolType
import net.minecraftforge.fluids.FluidUtil
import phoenix.api.block.BlockWithContainer
import phoenix.init.PhxSounds
import phoenix.network.SyncOvenPacket
import phoenix.network.sendToAllPlayers
import phoenix.recipes.OvenRecipe
import phoenix.tile.ash.OvenTile
import phoenix.utils.get
import phoenix.utils.getTileAt
import phoenix.utils.set
import java.util.*

class OvenBlock : BlockWithContainer(Properties.create(Material.ROCK).notSolid().hardnessAndResistance(10f).harvestTool(ToolType.PICKAXE))
{
    companion object
    {
        val STATE : IntegerProperty = IntegerProperty.create("state", 0, 2)
        val SHAPE   : VoxelShape    = Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 32.0, 16.0)
    }

    init
    {
        defaultState = stateContainer.baseState.with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).with(STATE, 0)
    }

    override fun onBlockActivated(state: BlockState, worldIn: World, pos: BlockPos, playerIn: PlayerEntity, handIn: Hand, hit: BlockRayTraceResult) : ActionResultType
    {
        if (!worldIn.isRemote)
        {
            val tile = worldIn.getTileAt<OvenTile>(pos)!!
            val stack: ItemStack = playerIn.getHeldItem(handIn)

            if (playerIn.heldItemMainhand.getItem() === Items.AIR)
            {
                if(worldIn[pos, STATE] != 2){
                    worldIn[pos, STATE] = 2
                }
                else
                {
                    worldIn[pos, STATE] = 1
                    val items = tile.getOutOtherItems()
                    for (i in items)
                    {
                        if (playerIn.addItemStackToInventory(i))
                            worldIn.playSound(playerIn, pos, PhxSounds.getItemFromOven, SoundCategory.BLOCKS, 1F, 1F)
                        else
                            worldIn.addEntity(ItemEntity(worldIn, playerIn.posX, (playerIn.posY + 1), playerIn.posZ, i))
                    }
                }
            }
            else if (OvenRecipe.recipesFromInputs[stack.getItem()] != null)
            {
                if(tile.addItem(stack))
                {
                    playerIn.getHeldItem(handIn).shrink(1)
                }
                SyncOvenPacket(tile).sendToAllPlayers()
            }
            else if (ForgeHooks.getBurnTime(stack) > 0 && !FluidUtil.getFluidContained(stack).isPresent)
            {
                tile.data[0] = ItemStack(stack.getItem(), 1)
                playerIn.getHeldItem(handIn).shrink(1)
                if(worldIn[pos, STATE] == 0)
                    worldIn[pos, STATE] = 1
            }
            return ActionResultType.CONSUME
        }

        return ActionResultType.PASS
    }

    override fun onBlockPlacedBy(
        worldIn: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack
    )
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack)
        worldIn[pos.up()] = Blocks.BARRIER.defaultState
    }

    override fun onPlayerDestroy(worldIn: IWorld, pos: BlockPos, state: BlockState)
    {
        super.onPlayerDestroy(worldIn, pos, state)
        worldIn[pos.up()] = Blocks.AIR.defaultState
    }

    override fun onReplaced(state: BlockState, worldIn: World, pos: BlockPos, newState: BlockState, isMoving: Boolean)
    {
        super.onReplaced(state, worldIn, pos, newState, isMoving)
        if(newState.block != this)
            worldIn[pos.up()] = Blocks.AIR.defaultState
    }

    override fun onBlockExploded(state: BlockState?, world: World, pos: BlockPos, explosion: Explosion?)
    {
        super.onBlockExploded(state, world, pos, explosion)
        world.setBlockState(pos.up(), Blocks.AIR.defaultState, 2)
    }

    override fun fillStateContainer(builder: StateContainer.Builder<Block, BlockState>)
    {
        builder.add(BlockStateProperties.HORIZONTAL_FACING).add(STATE)
    }

    override fun getStateForPlacement(context: BlockItemUseContext): BlockState = defaultState.with(BlockStateProperties.HORIZONTAL_FACING, context.placementHorizontalFacing.opposite)
    override fun getRenderType(state: BlockState) = BlockRenderType.MODEL
    override fun createNewTileEntity(worldIn: IBlockReader): TileEntity = OvenTile()

    @OnlyIn(Dist.CLIENT)
    override fun animateTick(stateIn: BlockState, worldIn: World, pos: BlockPos, rand: Random)
    {
        if ((worldIn.getTileEntity(pos) as OvenTile).data.isBurning())
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

    override fun getDrops(state: BlockState, builder: LootContext.Builder): List<ItemStack> = listOf(ItemStack(this))

    override fun getShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext) = SHAPE
    override fun getCollisionShape(state: BlockState, worldIn: IBlockReader, pos: BlockPos, context: ISelectionContext) = SHAPE


}