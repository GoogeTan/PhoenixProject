package phoenix.items.ash

import com.google.common.collect.ImmutableSet
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.enchantment.Enchantments
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.IItemTier
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.tags.Tag
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.Tags
import net.minecraftforge.event.ForgeEventFactory
import phoenix.Phoenix
import phoenix.enity.KnifeEntity
import phoenix.init.events.PhoenixEventsOther.addTask
import phoenix.utils.WorldUtils
import java.util.function.Consumer


class KnifeItem(tier: IItemTier, attackDamageIn: Float, attackSpeedIn: Float, maxUsages: Int) : ToolItem(attackDamageIn, attackSpeedIn, tier, breakableBlocks, Properties().group(Phoenix.PHOENIX).maxDamage(maxUsages))
{
    val damage: Float = attackDamageIn + tier.attackDamage

    override fun onItemRightClick(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack>
    {
        val itemstack = player.getHeldItem(hand)
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f))
        player.cooldownTracker.setCooldown(this, 10)
        if (!world.isRemote)
        {
            val knife = KnifeEntity(world, player, true)
            knife.knife = itemstack
            knife.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 1.5f, 1.0f)
            world.addEntity(knife)
            val count = EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, itemstack)
            for (i in 0..count)
            {
                addTask(10 * i) {
                    val knife2 = KnifeEntity(world, player, false)
                    knife2.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 1.5f, 1.0f)
                    world.addEntity(knife2)
                }
            }
            player.setHeldItem(hand, ItemStack.EMPTY)
        }

        return ActionResult(ActionResultType.SUCCESS, itemstack)
    }

    fun onHitBlock(world: World, owner: LivingEntity, pos: BlockPos, knife: KnifeEntity, item: ItemStack): Boolean
    {
        var shouldBroke = false
        val block = world.getBlockState(pos).block
        for (tag in breakableBlocksTypes) if (block.isIn(tag))
        {
            shouldBroke = true
            break
        }
        if (breakableBlocks.contains(world.getBlockState(pos).block) || shouldBroke)
        {
            WorldUtils.destroyBlock(world, pos, true, owner, item)
            knife.knife.attemptDamageItem(1, world.rand, null)
        }
        item.damageItem(1, owner, { p: LivingEntity? -> world.playSound(null, owner.position, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f)) })
        return !(block !== Blocks.GRASS_BLOCK && block !== Blocks.SNOW && block.isIn(Tags.Blocks.SAND))
    }

    fun onHitEntity(world: World, owner: LivingEntity, knife: KnifeEntity, hitted: Entity, knifeItem: ItemStack): Boolean
    {
        val powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, knifeItem)
        val damage = damage + powerLevel.toDouble() * 0.6
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, knifeItem) > 0 && hitted.type !== EntityType.SNOW_GOLEM) hitted.setFire(100)
        hitted.attackEntityFrom(DamageSource.causeThrownDamage(knife, knife.thrower), damage.toFloat())
        return hitted.type !== EntityType.SNOW_GOLEM && hitted.type !== EntityType.END_CRYSTAL && hitted.type !== EntityType.PANDA
    }

    override fun isEnchantable(stack: ItemStack) = true

    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment) = allowedEnchantments.contains(enchantment)

    companion object
    {
        var breakableBlocks: Set<Block> = ImmutableSet.of(Blocks.SPONGE, Blocks.VINE, Blocks.SEA_PICKLE, Blocks.WET_SPONGE, Blocks.GRASS, Blocks.TALL_GRASS)
        var breakableBlocksTypes: Set<Tag<Block>> = ImmutableSet.of(Tags.Blocks.GLASS, Tags.Blocks.STAINED_GLASS_PANES)
        var allowedEnchantments: Set<Enchantment> = ImmutableSet.of(Enchantments.POWER, Enchantments.QUICK_CHARGE, Enchantments.MENDING, Enchantments.FLAME, Enchantments.SILK_TOUCH, Enchantments.UNBREAKING)
    }
}