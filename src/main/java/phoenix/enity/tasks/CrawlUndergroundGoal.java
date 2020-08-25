package phoenix.enity.tasks;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.MathHelper;
import phoenix.enity.TalpaEntity;

public class CrawlUndergroundGoal extends Goal
{
    TalpaEntity entity;

    public CrawlUndergroundGoal(TalpaEntity entity)
    {
        this.entity = entity;
    }

    @Override
    public boolean shouldExecute()
    {
        return entity.isEntityInsideOpaqueBlock();
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return entity.isEntityInsideOpaqueBlock();
    }

    @Override
    public void tick()
    {
        if (this.entity.getRNG().nextInt(50) == 0)
        {
            float f = this.entity.getRNG().nextFloat() * ((float)Math.PI * 2F);
            float f1 = MathHelper.cos(f) * 0.2F;
            float f2 = -0.1F + this.entity.getRNG().nextFloat() * 0.2F;
            float f3 = MathHelper.sin(f) * 0.2F;
            this.entity.setMovementVector(f1, f2, f3);
        }
    }
}
