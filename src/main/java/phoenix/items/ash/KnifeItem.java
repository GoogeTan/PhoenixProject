package phoenix.items.ash;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.stats.Stat;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import phoenix.Phoenix;
import phoenix.enity.KnifeEntity;

import java.util.Set;

public class KnifeItem extends ToolItem
{
    static Set<Block> breakableBlocks = ImmutableSet.of(Blocks.SPONGE, Blocks.VINE, Blocks.SEA_PICKLE, Blocks.WET_SPONGE, Blocks.GLASS);
    public KnifeItem(IItemTier tier, float attackDamageIn, float attackSpeedIn)
    {
        super(attackDamageIn, attackSpeedIn, tier, breakableBlocks, new Item.Properties().group(Phoenix.PHOENIX));
    }

    //Метод вызывается, когда игрок кликет ПКМ с предметом
    @NotNull
    public ActionResult<ItemStack> onItemRightClick(@NotNull World world, PlayerEntity player, @NotNull Hand hand)
    {
        //Получаем стак в руке игрока
        ItemStack itemstack = player.getHeldItem(hand);

        //Проверяем, что игрок НЕ в творческом режиме
        if (!player.isCreative()) {
            //Уменьшаем стак на 1
            itemstack.shrink(1);
        }

        //Проигрываем звук
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        //Делаем задержку между бросаниями
        player.getCooldownTracker().setCooldown(this, 10);

        if (!world.isRemote)
        {
            //Создаём объект нашего летающего яблока
            KnifeEntity knife = new KnifeEntity(world, player);
            knife.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.addEntity(knife);
        }

        //Возвращаем успешное действие, то-есть при ПКМ рука дёрнется как будто игрок кинул предмет
        return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
    }





    public boolean onHitBlock(IWorld world, BlockPos pos, KnifeEntity knife, ItemStack item)
    {
        if(breakableBlocks.contains(world.getBlockState(pos).getBlock()))
        {
            world.destroyBlock(pos, true);
        }
        return true;
    }

    public boolean onHitEntity(IWorld world, KnifeEntity knife, Entity hitted, ItemStack knifeItem)
    {
        int damage = hitted instanceof SnowGolemEntity ? 0 : knifeItem.getDamage();
        hitted.attackEntityFrom(DamageSource.causeThrownDamage(knife, knife.getThrower()), (float)damage);
        return true;
    }
}
