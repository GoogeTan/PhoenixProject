package phoenix.utils.entity;


import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public abstract class ThreeDimensionsMovingGoal extends Goal
{
    protected AbstractFlyingEntity entity;
    public ThreeDimensionsMovingGoal(AbstractFlyingEntity entityIn)
    {
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        entity = entityIn;
    }

    protected boolean func_203146_f()
    {
        return entity.orbitOffset.squareDistanceTo(entity.getPosX(), entity.getPosY(), entity.getPosZ()) < 4.0D;
    }
}