package phoenix.enity.tasks;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.Vec3d;
import phoenix.utils.entity.AbstractFlyingEntity;
import phoenix.utils.entity.AttackPhases;
import phoenix.utils.entity.ThreeDimensionsMovingGoal;

import java.util.List;

public class SweepAttackGoal extends ThreeDimensionsMovingGoal
{
    public SweepAttackGoal(AbstractFlyingEntity entityIn)
    {
        super(entityIn);
    }

    public boolean shouldExecute()
    {
        return entity.getAttackTarget() != null && entity.attackPhase == AttackPhases.SWOOP;
    }

    public boolean shouldContinueExecuting()
    {
        LivingEntity livingentity = entity.getAttackTarget();
        if (livingentity == null)
        {
            return false;
        }
        else if (!livingentity.isAlive())
        {
            return false;
        }
        else if (!(livingentity instanceof PlayerEntity) || !livingentity.isSpectator() && !((PlayerEntity) livingentity).isCreative())
        {
            if (!this.shouldExecute())
            {
                return false;
            }
            else
            {
                if (entity.ticksExisted % 20 == 0)
                {
                    List<CatEntity> list = entity.world.getEntitiesWithinAABB(CatEntity.class, entity.getBoundingBox().grow(16.0D), EntityPredicates.IS_ALIVE);
                    if (!list.isEmpty())
                    {
                        for (CatEntity catentity : list)
                        {
                            catentity.func_213420_ej();
                        }

                        return false;
                    }
                }

                return true;
            }
        }
        else
        {
            return false;
        }
    }


    public void startExecuting()
    {
    }


    public void resetTask()
    {
        entity.setAttackTarget(null);
        entity.attackPhase = AttackPhases.CIRCLE;
    }

    public void tick()
    {
        LivingEntity livingentity = entity.getAttackTarget();
        entity.orbitOffset = new Vec3d(livingentity.getPosX(), livingentity.getPosYHeight(0.5D), livingentity.getPosZ());
        if (entity.getBoundingBox().grow(0.2F).intersects(livingentity.getBoundingBox()))
        {
            entity.attackEntityAsMob(livingentity);
            entity.attackPhase = AttackPhases.CIRCLE;
        } else if (entity.collidedHorizontally || entity.hurtTime > 0)
        {
            entity.attackPhase = AttackPhases.CIRCLE;
        }

    }
}