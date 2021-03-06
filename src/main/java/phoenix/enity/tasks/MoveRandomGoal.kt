package phoenix.enity.tasks

import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.BlockPos
import phoenix.enity.TalpaEntity
import java.util.*


class MoveRandomGoal(entity: TalpaEntity) : Goal()
{
    var entity: TalpaEntity

    /**
     * Returns whether execution should begin. You can also read and cache any STATE necessary for execution in this
     * method as well.
     */
    override fun shouldExecute(): Boolean = !entity.moveHelper.isUpdating && entity.rng.nextInt(7) == 0

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    override fun shouldContinueExecuting(): Boolean = false

    /**
     * Keep ticking a continuous task that has already been started
     */
    override fun tick()
    {
        var blockpos = entity.boundOrigin
        if (blockpos == null)
        {
            blockpos = BlockPos(entity)
        }
        for (i in 0..2)
        {
            val randompoint =
                blockpos.add(entity.rng.nextInt(15) - 7, entity.rng.nextInt(11) - 5, entity.rng.nextInt(15) - 7)
            if (entity.world.isAirBlock(randompoint))
            {
                entity.moveHelper.setMoveTo(
                    randompoint.x.toDouble() + 0.5, randompoint.y.toDouble() + 0.5, randompoint.z
                        .toDouble() + 0.5, 0.25
                )
                if (entity.attackTarget == null)
                {
                    entity.lookController.setLookPosition(
                        randompoint.x.toDouble() + 0.5, randompoint.y.toDouble() + 0.5, randompoint.z
                            .toDouble() + 0.5, 180.0f, 20.0f
                    )
                }
                break
            }
        }
    }

    init
    {
        this.mutexFlags = EnumSet.of(Flag.MOVE)
        this.entity = entity
    }
}
