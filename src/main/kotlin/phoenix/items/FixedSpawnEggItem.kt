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
import phoenix.other.get
import phoenix.other.getTileAt

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
            ActionResultType.SUCCESS
        else
        {
            val stack = context.item
            val pos: BlockPos = context.pos
            val dir = context.face
            val state = world[pos]
            val block = state.block
            if (block === Blocks.SPAWNER)
            {
                val spawner = world.getTileAt<MobSpawnerTileEntity>(pos)
                if (spawner != null)
                {
                    val spawnerLogic = spawner.spawnerBaseLogic
                    spawnerLogic.setEntityType(getType(stack.tag))
                    spawner.markDirty()
                    world.notifyBlockUpdate(pos, state, state, 3)
                    stack.shrink(1)
                    return ActionResultType.SUCCESS
                }
            }
            val spawnPos: BlockPos = if (state.getCollisionShape(world, pos).isEmpty) pos else pos.offset(dir)

            val entityType = getType(stack.tag)
            if (entityType.spawn(world, stack, context.player, spawnPos, SpawnReason.SPAWN_EGG, true, pos != spawnPos && dir == Direction.UP) != null)
                stack.shrink(1)
            ActionResultType.SUCCESS
        }
    }

    /**
     * Called to trigger the item's "innate" right click behavior. To handle when this item is used on a Block, see
     * [.onItemUse].
     */
    override fun onItemRightClick(worldIn: World, playerIn: PlayerEntity, handIn: Hand): ActionResult<ItemStack>
    {
        val stack = playerIn.getHeldItem(handIn)
        val trace = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.SOURCE_ONLY)
        return if (trace.type != RayTraceResult.Type.BLOCK)
        {
            ActionResult.resultPass(stack)
        } else if (worldIn.isRemote)
        {
            ActionResult.resultSuccess(stack)
        } else
        {
            val blockTrace = trace as BlockRayTraceResult
            val pos = blockTrace.pos
            if (worldIn[pos].block !is FlowingFluidBlock)
            {
                ActionResult.resultPass(stack)
            } else if (worldIn.isBlockModifiable(playerIn, pos) && playerIn.canPlayerEdit(pos, blockTrace.face, stack))
            {
                val entitytype = getType(stack.tag)
                if (entitytype.spawn(worldIn, stack, playerIn, pos, SpawnReason.SPAWN_EGG, false, false) == null)
                    ActionResult.resultPass(stack)
                else
                {
                    if (!playerIn.abilities.isCreativeMode)
                    {
                        stack.shrink(1)
                    }
                    playerIn.addStat(Stats.ITEM_USED[this])
                    ActionResult.resultSuccess(stack)
                }
            } else
            {
                ActionResult.resultFail(stack)
            }
        }
    }

    @OnlyIn(Dist.CLIENT) fun getColor(tintIndex: Int): Int = if (tintIndex == 0) primaryColor else secondaryColor
    @OnlyIn(Dist.CLIENT) fun getColor(stack: ItemStack, tintIndex: Int): Int = if (tintIndex == 0) primaryColor else secondaryColor

    private fun getType(tag: CompoundNBT?): EntityType<*>
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

    override fun toString(): String = "FixedSpawnEggItem(typeIn=$typeIn, primaryColor=$primaryColor, secondaryColor=$secondaryColor)"

    companion object
    {
        private val EGGS: MutableMap<() -> EntityType<*>, FixedSpawnEggItem> = Maps.newIdentityHashMap()

        @OnlyIn(Dist.CLIENT) fun getEgg(type: EntityType<*>): FixedSpawnEggItem? = EGGS[{ type }]
        @OnlyIn(Dist.CLIENT) fun getEgg(type: () -> EntityType<*>): FixedSpawnEggItem? = EGGS[type]

        val eggs: Iterable<FixedSpawnEggItem>
            get() = Iterables.unmodifiableIterable(EGGS.values)
    }

    init
    {
        EGGS[typeIn] = this
    }
}
