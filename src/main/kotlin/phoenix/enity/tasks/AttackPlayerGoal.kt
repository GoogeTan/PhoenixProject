package phoenix.enity.tasks

import net.minecraft.entity.EntityPredicate
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.player.PlayerEntity
import phoenix.utils.entity.AbstractFlyingEntity


class AttackPlayerGoal(var entity: AbstractFlyingEntity) : Goal()
{
    private val field_220842_b = EntityPredicate().setDistance(64.0)
    private var tickDelay = 20

    /**
     * Returns whether execution should begin. You can also read and cache any STATE necessary for execution in this
     * method as well.
     */
    override fun shouldExecute(): Boolean
    {
        if (tickDelay > 0)
        {
            --tickDelay
        } else
        {
            tickDelay = 60
            val list = entity.world.getTargettablePlayersWithinAABB(
                field_220842_b,
                entity,
                entity.boundingBox.grow(16.0, 64.0, 16.0)
            )
            if (list.isNotEmpty())
            {
                list.sortWith { player1: PlayerEntity, player2: PlayerEntity -> if (player1.posY > player2.posY) -1 else 1 }
                for (player in list)
                {
                    if (entity.canAttack(player, EntityPredicate.DEFAULT))
                    {
                        entity.attackTarget = player
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    override fun shouldContinueExecuting(): Boolean = entity.attackTarget != null && entity.canAttack(entity.attackTarget, EntityPredicate.DEFAULT)
}