package phoenix.items

import com.google.common.collect.Iterables
import com.google.common.collect.Maps
import net.minecraft.block.Blocks
import net.minecraft.block.FlowingFluidBlock
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnReason
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUseContext
import net.minecraft.nbt.CompoundNBT
import net.minecraft.stats.Stats
import net.minecraft.tileentity.MobSpawnerTileEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.ActionResultType
import net.minecraft.util.Direction
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.util.math.RayTraceContext
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.World
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

class FixedSpawnEggItem
    (
        private val typeIn: () -> EntityType<*>,
        private val primaryColor: Int,
        private val secondaryColor: Int,
        builder: Properties
    ) : Item(builder)
{
    /**
     * Called when this item is used when targetting a Block
     */
    override fun onItemUse(context: ItemUseContext): ActionResultType
    {
        val world = context.world
        return if (world.isRemote)
        {
            ActionResultType.SUCCESS
        } else
        {
            val itemstack = context.item
            val blockpos = context.pos
            val direction = context.face
            val blockstate = world.getBlockState(blockpos)
            val block = blockstate.block
            if (block === Blocks.SPAWNER)
            {
                val tileentity = world.getTileEntity(blockpos)
                if (tileentity is MobSpawnerTileEntity)
                {
                    val abstractspawner = tileentity.spawnerBaseLogic
                    val entitytype1 = getType(itemstack.tag)
                    abstractspawner.setEntityType(entitytype1)
                    tileentity.markDirty()
                    world.notifyBlockUpdate(blockpos, blockstate, blockstate, 3)
                    itemstack.shrink(1)
                    return ActionResultType.SUCCESS
                }
            }
            val blockpos1: BlockPos
            blockpos1 = if (blockstate.getCollisionShape(world, blockpos).isEmpty)
            {
                blockpos
            } else
            {
                blockpos.offset(direction)
            }
            val entitytype = getType(itemstack.tag)
            if (entitytype.spawn(
                    world,
                    itemstack,
                    context.player,
                    blockpos1,
                    SpawnReason.SPAWN_EGG,
                    true,
                    blockpos != blockpos1 && direction == Direction.UP
                ) != null
            )
            {
                itemstack.shrink(1)
            }
            ActionResultType.SUCCESS
        }
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * [.onItemUse].
     */
    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack>
    {
        val itemstack = playerIn.getHeldItem(handIn)
        val raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY)
        return if (raytraceresult.type != RayTraceResult.Type.BLOCK)
        {
            ActionResult.resultPass(itemstack)
        } else if (worldIn.isRemote)
        {
            ActionResult.resultSuccess(itemstack)
        } else
        {
            val blockraytraceresult = raytraceresult as BlockRayTraceResult
            val blockpos = blockraytraceresult.pos
            if (worldIn.getBlockState(blockpos).block !is FlowingFluidBlock)
            {
                ActionResult.resultPass(itemstack)
            } else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(
                    blockpos,
                    blockraytraceresult.face,
                    itemstack
                )
            )
            {
                val entitytype = getType(itemstack.tag)
                if (entitytype.spawn(
                        worldIn,
                        itemstack,
                        playerIn,
                        blockpos,
                        SpawnReason.SPAWN_EGG,
                        false,
                        false
                    ) == null
                )
                {
                    ActionResult.resultPass(itemstack)
                } else
                {
                    if (!playerIn.abilities.isCreativeMode)
                    {
                        itemstack.shrink(1)
                    }
                    playerIn.addStat(Stats.ITEM_USED[this])
                    ActionResult.resultSuccess(itemstack)
                }
            } else
            {
                ActionResult.resultFail(itemstack)
            }
        }
    }

    fun hasType(tag: CompoundNBT?, type: EntityType<*>?): Boolean
    {
        return getType(tag) == type
    }

    @OnlyIn(Dist.CLIENT)
    fun getColor(tintIndex: Int): Int
    {
        return if (tintIndex == 0) primaryColor else secondaryColor
    }

    fun getType(tag: CompoundNBT?): EntityType<*>
    {
        if (tag != null && tag.contains("EntityTag", 10))
        {
            val compoundnbt = tag.getCompound("EntityTag")
            if (compoundnbt.contains("id", 8))
            {
                return EntityType.byKey(compoundnbt.getString("id")).orElse(typeIn())
            }
        }
        return typeIn()
    }

    companion object
    {
        private val EGGS: MutableMap<() -> EntityType<*>, FixedSpawnEggItem> = Maps.newIdentityHashMap()

        @OnlyIn(Dist.CLIENT)
        fun getEgg(type: EntityType<*>): FixedSpawnEggItem = EGGS[{ type }]!!
        @OnlyIn(Dist.CLIENT)
        fun getEgg(type: () -> EntityType<*>): FixedSpawnEggItem = EGGS[type]!!

        val eggs: Iterable<FixedSpawnEggItem>
            get() = Iterables.unmodifiableIterable(EGGS.values)
    }

    init
    {
        EGGS[typeIn] = this
    }
}
