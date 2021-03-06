package phoenix.enity.tasks

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.EntityPredicates
import net.minecraft.util.math.Vec3d
import phoenix.utils.entity.AbstractFlyingEntity
import phoenix.utils.entity.AttackPhases
import phoenix.utils.entity.ThreeDimensionsMovingGoal


class SweepAttackGoal(entityIn: AbstractFlyingEntity?) : ThreeDimensionsMovingGoal(entityIn)
{
    override fun shouldExecute(): Boolean
    {
        return entity.attackTarget != null && entity.attackPhase == AttackPhases.SWOOP
    }

    override fun shouldContinueExecuting(): Boolean
    {
        val livingentity = entity.attackTarget
        return if (livingentity == null)
        {
            false
        } else if (!livingentity.isAlive)
        {
            false
        } else if (livingentity !is PlayerEntity || !livingentity.isSpectator() && !livingentity.isCreative)
        {
            if (!shouldExecute())
            {
                false
            } else
            {
                if (entity.ticksExisted % 20 == 0)
                {
                    val list = entity.world.getEntitiesWithinAABB(
                        CatEntity::class.java, entity.boundingBox.grow(16.0), EntityPredicates.IS_ALIVE
                    )
                    if (!list.isEmpty())
                    {
                        for (catentity in list)
                        {
                            catentity.func_213420_ej()
                        }
                        return false
                    }
                }
                true
            }
        } else
        {
            false
        }
    }

    override fun startExecuting()
    {
    }

    override fun resetTask()
    {
        entity.attackTarget = null
        entity.attackPhase = AttackPhases.CIRCLE
    }

    override fun tick()
    {
        val livingentity = entity.attackTarget
        entity.orbitOffset = Vec3d(livingentity!!.posX, livingentity.getPosYHeight(0.5), livingentity.posZ)
        if (entity.boundingBox.grow(0.2).intersects(livingentity.boundingBox))
        {
            entity.attackEntityAsMob(livingentity)
            entity.attackPhase = AttackPhases.CIRCLE
        } else if (entity.collidedHorizontally || entity.hurtTime > 0)
        {
            entity.attackPhase = AttackPhases.CIRCLE
        }
    }
}