package phoenix.items.ash

import com.google.common.collect.ImmutableSet
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.Enchantments
import net.minecraft.enchantment.Enchantments.QUICK_CHARGE
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.IItemTier
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolItem
import net.minecraft.tags.Tag
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Explosion
import net.minecraft.world.World
import net.minecraftforge.common.Tags
import phoenix.enity.KnifeEntity
import phoenix.init.PhoenixEnchantments
import phoenix.init.events.PhoenixEvents.addTask
import phoenix.utils.destroyBlock
import phoenix.utils.getEnchantmentLevel

class KnifeItem(tier: IItemTier, attackDamageIn: Float, attackSpeedIn: Float, maxUsages: Int, group : ItemGroup) : ToolItem(attackDamageIn, attackSpeedIn, tier, breakableBlocks, Properties().group(group).maxDamage(maxUsages))
{
    private val damage: Float = attackDamageIn + tier.attackDamage

    override fun onItemRightClick(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack>
    {
        val stack = player.getHeldItem(hand)
        world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (random.nextFloat() * 0.4f + 0.8f))
        var coolDown = 10
        coolDown -= (1.5 * stack.getEnchantmentLevel(QUICK_CHARGE)).toInt()
        player.cooldownTracker.setCooldown(this, coolDown)
        if (!world.isRemote)
        {
            val knife = KnifeEntity(world, player, stack.getEnchantmentLevel(Enchantments.INFINITY) == 0)
            knife.knife = stack
            knife.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 2f, 0.3f)
            world.addEntity(knife)
            val count = stack.getEnchantmentLevel(Enchantments.MULTISHOT)
            for (i in 1..count)
            {
                addTask(10 * i)
                {
                    val knife2 = KnifeEntity(world, player, false)
                    knife2.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 2.5f, 1.0f)
                    world.addEntity(knife2)
                }
            }
            if(stack.getEnchantmentLevel(Enchantments.INFINITY) == 0)
                player.setHeldItem(hand, ItemStack.EMPTY)
        }

        return ActionResult(ActionResultType.SUCCESS, stack)
    }

    fun onHitBlock(world: World, owner: LivingEntity?, pos: BlockPos, knife: KnifeEntity, item: ItemStack): Boolean
    {
        var shouldBroke = false
        val block = world.getBlockState(pos).block
        for (tag in breakableBlocksTypes) if (block.isIn(tag))
        {
            shouldBroke = true
            break
        }
        if(block == Blocks.TNT && item.getEnchantmentLevel(Enchantments.FLAME) > 0)
        {
            world.destroyBlock(pos, false, owner, item)
            world.createExplosion(knife, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 5F, true, Explosion.Mode.BREAK)

            return false
        }
        else if (breakableBlocks.contains(world.getBlockState(pos).block) || shouldBroke)
        {
            world.destroyBlock(pos, true, owner, item)
            knife.knife.attemptDamageItem(1, world.rand, null)
        }

        if(item.getEnchantmentLevel(PhoenixEnchantments.TELEPORTATION.get()) > 0 && owner != null)
        {
            owner.setPositionAndUpdate(pos.up().x.toDouble(), pos.up().y.toDouble(), pos.up().z.toDouble())
            owner.fallDistance = 0.0f
        }

        return !(block !== Blocks.SNOW && block.isIn(Tags.Blocks.SAND))
    }

    fun onHitEntity(world : World, owner : LivingEntity?, knife : KnifeEntity, hitted : Entity, knifeItem : ItemStack): Boolean
    {
        val powerLevel = knifeItem.getEnchantmentLevel(Enchantments.POWER)
        val damage = damage + powerLevel.toDouble() * 0.6
        if (knifeItem.getEnchantmentLevel(Enchantments.FLAME) > 0 && hitted.type !== EntityType.SNOW_GOLEM)
            hitted.setFire(100)

        hitted.attackEntityFrom(DamageSource.causeThrownDamage(knife, knife.thrower), damage.toFloat())
        if(knifeItem.getEnchantmentLevel(PhoenixEnchantments.TELEPORTATION.get()) > 0 && owner != null)
        {
            owner.setPositionAndUpdate(hitted.posX, hitted.posY, hitted.posZ)
            owner.fallDistance = 0.0f
        }

        return hitted.type !== EntityType.SNOW_GOLEM && hitted.type !== EntityType.END_CRYSTAL && hitted.type !== EntityType.PANDA
    }

    override fun isEnchantable(stack: ItemStack) = true

    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment) = allowedEnchantments.contains(enchantment) || enchantment == PhoenixEnchantments.TELEPORTATION.get()

    companion object
    {
        var breakableBlocks      : Set<Block>       = ImmutableSet.of(Blocks.SPONGE, Blocks.VINE, Blocks.SEA_PICKLE, Blocks.WET_SPONGE, Blocks.GRASS, Blocks.TALL_GRASS, Blocks.SUGAR_CANE, Blocks.TALL_SEAGRASS)
        var breakableBlocksTypes : Set<Tag<Block>>  = ImmutableSet.of(Tags.Blocks.GLASS, Tags.Blocks.STAINED_GLASS_PANES)
        var allowedEnchantments  : Set<Enchantment> = ImmutableSet.of(Enchantments.POWER, QUICK_CHARGE, Enchantments.MENDING, Enchantments.FLAME, Enchantments.SILK_TOUCH, Enchantments.UNBREAKING)
    }
}