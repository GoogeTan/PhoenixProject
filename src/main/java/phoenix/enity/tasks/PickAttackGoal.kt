package phoenix.enity.tasks

import net.minecraft.entity.EntityPredicate
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.Heightmap
import phoenix.utils.entity.AbstractFlyingEntity
import phoenix.utils.entity.AttackPhases


class PickAttackGoal(var entity: AbstractFlyingEntity) : Goal()
{
    private var tickDelay = 0
    override fun shouldExecute(): Boolean = entity.attackTarget != null && entity.canAttack(entity.attackTarget, EntityPredicate.DEFAULT)

    override fun startExecuting()
    {
        tickDelay = 10
        entity.attackPhase = AttackPhases.CIRCLE
        updateOrbitPosition()
    }

    override fun resetTask()
    {
        entity.orbitPosition = entity.world.getHeight(Heightmap.Type.MOTION_BLOCKING, entity.orbitPosition).up(10 + entity.rng.nextInt(20))
    }

    override fun tick()
    {
        if (entity.attackPhase == AttackPhases.CIRCLE)
        {
            --tickDelay
            if (tickDelay <= 0)
            {
                entity.attackPhase = AttackPhases.SWOOP
                updateOrbitPosition()
                tickDelay = (8 + entity.rng.nextInt(4)) * 20
            }
        }
    }

    private fun updateOrbitPosition()
    {
        entity.orbitPosition = BlockPos(entity.attackTarget).up(20 + entity.rng.nextInt(20))
        if (entity.orbitPosition.y < entity.world.seaLevel)
        {
            entity.orbitPosition = BlockPos(entity.orbitPosition.x, entity.world.seaLevel + 1, entity.orbitPosition.z)
        }
    }
}