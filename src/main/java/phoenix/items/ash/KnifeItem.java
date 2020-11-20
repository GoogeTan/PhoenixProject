package phoenix.items.ash;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.tags.Tag;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import phoenix.Phoenix;
import phoenix.enity.KnifeEntity;
import phoenix.init.events.PhoenixCommonEvents;
import phoenix.init.events.PhoenixEventsOther;
import phoenix.utils.WorldUtils;

import java.util.List;
import java.util.Set;

public class KnifeItem extends ToolItem
{
    static Set<Block> breakableBlocks = ImmutableSet.of(Blocks.SPONGE, Blocks.VINE, Blocks.SEA_PICKLE, Blocks.WET_SPONGE);
    static Set<Tag<Block>> breakableBlocksTypes = ImmutableSet.of(Tags.Blocks.GLASS, Tags.Blocks.STAINED_GLASS_PANES);
    public final float damage;
    public KnifeItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, int maxUsages)
    {
        super(attackDamageIn, attackSpeedIn, tier, breakableBlocks, new Item.Properties().group(Phoenix.PHOENIX).maxDamage(maxUsages));
        damage = attackDamageIn + tier.getAttackDamage();
    }

    //Метод вызывается, когда игрок кликет ПКМ с предметом
    @NotNull
    public ActionResult<ItemStack> onItemRightClick(@NotNull World world, PlayerEntity player, @NotNull Hand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        int coolDown = 10;
        int speed = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, itemstack);
        coolDown = (int) (coolDown * (double) speed / 3);
        player.getCooldownTracker().setCooldown(this, coolDown);

        if (!world.isRemote)
        {
            KnifeEntity knife = new KnifeEntity(world, player);
            knife.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.addEntity(knife);
            int count = EnchantmentHelper.getEnchantmentLevel(Enchantments.MULTISHOT, itemstack) + 1;
            for (int i = 0; i < count; ++i)
            {
                PhoenixEventsOther.addTask(5, () ->
                {
                    KnifeEntity knife2 = new KnifeEntity(world, player, false);
                    knife2.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
                    world.addEntity(knife2);
                });
            }
        }
        itemstack.setCount(0);
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }

    @Override
    public void addInformation(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_)
    {
        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
    }

    public boolean onHitBlock(World world, LivingEntity owner, BlockPos pos, KnifeEntity knife, ItemStack item)
    {
        boolean shouldBroke = false;
        Block block = world.getBlockState(pos).getBlock();
        for (Tag<Block> tag : breakableBlocksTypes)
            if(block.isIn(tag))
            {
                shouldBroke = true;
                break;
            }
        if(breakableBlocks.contains(world.getBlockState(pos).getBlock()) || shouldBroke)
        {
            WorldUtils.destroyBlock(world, pos, true, owner, item);
        }
        item.damageItem(1, owner, (p) -> world.playSound(null, owner.getPosition(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F)));

        return !(block != Blocks.SNOW_BLOCK && block != Blocks.SNOW && block.isIn(Tags.Blocks.SAND));
    }

    public boolean onHitEntity(World world, LivingEntity owner, KnifeEntity knife, Entity hitted, ItemStack knifeItem)
    {

        int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, knifeItem);

        double damage = this.damage + (double)powerLevel * 0.6D;

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, knifeItem) > 0 && hitted.getType() != EntityType.SNOW_GOLEM)
            hitted.setFire(100);

        hitted.attackEntityFrom(DamageSource.causeThrownDamage(knife, knife.getThrower()), (float)damage);
        return hitted.getType() != EntityType.SNOW_GOLEM && hitted.getType() != EntityType.END_CRYSTAL && hitted.getType() != EntityType.PANDA;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment == Enchantments.POWER || enchantment == Enchantments.QUICK_CHARGE || enchantment == Enchantments.MENDING || enchantment == Enchantments.FLAME
                || enchantment == Enchantments.SILK_TOUCH || enchantment == Enchantments.UNBREAKING;
    }

}