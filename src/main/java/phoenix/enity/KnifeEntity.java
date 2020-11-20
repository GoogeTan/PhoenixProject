package phoenix.enity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import phoenix.init.PhoenixEntities;
import phoenix.init.PhoenixItems;
import phoenix.items.ash.KnifeItem;

public class KnifeEntity extends ThrowableEntity
{
    public ItemStack knife = new ItemStack(PhoenixItems.ZIRCONIUM_KNIFE.get());
    public boolean isReal = true;
    public static KnifeEntity create(EntityType<? extends ThrowableEntity> type, World worldIn)
    {
        return new KnifeEntity(worldIn, null);
    }

    public KnifeEntity(World worldIn, LivingEntity owner)
    {
        super(PhoenixEntities.KNIFE.get(), owner, worldIn);
    }
    public KnifeEntity(World worldIn, LivingEntity owner, boolean isReal)
    {
        super(PhoenixEntities.KNIFE.get(), owner, worldIn);
        this.isReal = isReal;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                //Появляются частицы лавы. В обычном майнкрафте они образуются на потолке, если сверху лава.
                world.addParticle(ParticleTypes.RAIN, getPosX(), getPosY(), getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onImpact(@NotNull RayTraceResult result)
    {
        if(!world.isRemote)
        {
            boolean dropItem = isReal;
            switch (result.getType())
            {
                case ENTITY:
                    dropItem &= ((KnifeItem) knife.getItem()).onHitEntity(world, owner, this, ((EntityRayTraceResult)result).getEntity(), knife);
                    break;
                case BLOCK:
                    dropItem &= ((KnifeItem) knife.getItem()).onHitBlock(world, owner, ((BlockRayTraceResult)result).getPos(), this, knife);
                    break;
            }
            if (dropItem)  world.addEntity(new ItemEntity(world, getPosX(), getPosY(), getPosZ(), knife));

            onKillCommand();
        }
    }

    @Override
    protected void registerData()
    {

    }
}
