package phoenix.enity.tasks

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import phoenix.utils.entity.AbstractFlyingEntity
import phoenix.utils.entity.AttackPhases
import phoenix.utils.entity.ThreeDimensionsMovingGoal


class OrbitPointGoal(entityIn: AbstractFlyingEntity) : ThreeDimensionsMovingGoal(entityIn)
{
    private var field_203150_c = 0f
    private var field_203151_d = 0f
    private var field_203152_e = 0f
    private var field_203153_f = 0f
    lateinit var entity: AbstractFlyingEntity

    /**
     * Returns whether execution should begin. You can also read and cache any STATE necessary for execution in this
     * method as well.
     */
    override fun shouldExecute(): Boolean
    {
        return entity.attackTarget == null || entity.attackPhase == AttackPhases.CIRCLE
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    override fun startExecuting()
    {
        field_203151_d = 5.0f + entity.rng.nextFloat() * 10.0f
        field_203152_e = -4.0f + entity.rng.nextFloat() * 9.0f
        field_203153_f = if (entity.rng.nextBoolean()) 1.0f else -1.0f
        func_203148_i()
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    override fun tick()
    {
        if (entity.rng.nextInt(350) == 0)
        {
            field_203152_e = -4.0f + entity.rng.nextFloat() * 9.0f
        }
        if (entity.rng.nextInt(250) == 0)
        {
            ++field_203151_d
            if (field_203151_d > 15.0f)
            {
                field_203151_d = 5.0f
                field_203153_f = -field_203153_f
            }
        }
        if (entity.rng.nextInt(450) == 0)
        {
            field_203150_c = entity.rng.nextFloat() * 2.0f * Math.PI.toFloat()
            func_203148_i()
        }
        if (this.isNear)
        {
            func_203148_i()
        }
        if (entity.orbitOffset.y < entity.posY && !entity.world.isAirBlock(BlockPos(entity).down(1)))
        {
            field_203152_e = 1.0f.coerceAtLeast(field_203152_e)
            func_203148_i()
        }
        if (entity.orbitOffset.y > entity.posY && !entity.world.isAirBlock(BlockPos(entity).up(1)))
        {
            field_203152_e = (-1.0f).coerceAtMost(field_203152_e)
            func_203148_i()
        }
    }

    private fun func_203148_i()
    {
        if (BlockPos.ZERO == entity.orbitPosition)
        {
            entity.orbitPosition = BlockPos(entity)
        }
        field_203150_c += field_203153_f * 15.0f * (Math.PI.toFloat() / 180f)
        entity.orbitOffset = Vec3d(entity.orbitPosition).add(
            (field_203151_d * MathHelper.cos(
                field_203150_c
            )).toDouble(),
            (-4.0f + field_203152_e).toDouble(),
            (field_203151_d * MathHelper.sin(field_203150_c)).toDouble()
        )
    }

    init
    {
        entity = entityIn
    }
}
