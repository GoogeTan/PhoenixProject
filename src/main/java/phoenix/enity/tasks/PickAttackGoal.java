package phoenix.enity.tasks;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap;
import phoenix.utils.entity.AbstractFlyingEntity;
import phoenix.utils.entity.AttackPhases;

public class PickAttackGoal extends Goal
{
    private int tickDelay;
    AbstractFlyingEntity entity;

    public PickAttackGoal(AbstractFlyingEntity entityIn)
    {
        entity = entityIn;
    }


    public boolean shouldExecute()
    {
        LivingEntity livingentity = entity.getAttackTarget();
        return livingentity != null && entity.canAttack(entity.getAttackTarget(), EntityPredicate.DEFAULT);
    }

    public void startExecuting()
    {
        this.tickDelay = 10;
        entity.attackPhase = AttackPhases.CIRCLE;
        this.updateOrbitPosition();
    }

    public void resetTask()
    {
        entity.orbitPosition = entity.world.getHeight(Heightmap.Type.MOTION_BLOCKING, entity.orbitPosition).up(10 + entity.getRNG().nextInt(20));
    }

    public void tick()
    {
        if (entity.attackPhase == AttackPhases.CIRCLE)
        {
            --this.tickDelay;
            if (this.tickDelay <= 0)
            {
                entity.attackPhase = AttackPhases.SWOOP;
                this.updateOrbitPosition();
                this.tickDelay = (8 + entity.getRNG().nextInt(4)) * 20;
                entity.playSound(SoundEvents.ENTITY_PHANTOM_SWOOP, 10.0F, 0.95F + entity.getRNG().nextFloat() * 0.1F);
            }
        }

    }

    private void updateOrbitPosition()
    {
        entity.orbitPosition = (new BlockPos(entity.getAttackTarget())).up(20 + entity.getRNG().nextInt(20));
        if (entity.orbitPosition.getY() < entity.world.getSeaLevel())
        {
            entity.orbitPosition = new BlockPos(entity.orbitPosition.getX(), entity.world.getSeaLevel() + 1, entity.orbitPosition.getZ());
        }

    }
}