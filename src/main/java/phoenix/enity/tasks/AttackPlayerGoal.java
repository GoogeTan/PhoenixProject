package phoenix.enity.tasks;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import phoenix.utils.entity.AbstractFlyingEntity;

import java.util.List;

public class AttackPlayerGoal extends Goal
{
    private final EntityPredicate field_220842_b = (new EntityPredicate()).setDistance(64.0D);
    private int tickDelay = 20;
    AbstractFlyingEntity entity;

    public AttackPlayerGoal(AbstractFlyingEntity entityIn)
    {
        entity = entityIn;
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    public boolean shouldExecute()
    {
        if (this.tickDelay > 0)
        {
            --this.tickDelay;
            return false;
        } else
        {
            this.tickDelay = 60;
            List<PlayerEntity> list = entity.world.getTargettablePlayersWithinAABB(this.field_220842_b, entity, entity.getBoundingBox().grow(16.0D, 64.0D, 16.0D));
            if (!list.isEmpty())
            {
                list.sort((player1, player2) -> player1.getPosY() > player2.getPosY() ? -1 : 1);

                for (PlayerEntity playerentity : list)
                {
                    if (entity.canAttack(playerentity, EntityPredicate.DEFAULT))
                    {
                        entity.setAttackTarget(playerentity);
                        return true;
                    }
                }
            }

            return false;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        LivingEntity livingentity = entity.getAttackTarget();
        return livingentity != null && entity.canAttack(livingentity, EntityPredicate.DEFAULT);
    }
}