package phoenix.utils.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.FlyingEntity
import net.minecraft.entity.MobEntity
import net.minecraft.entity.ai.controller.LookController
import net.minecraft.entity.ai.controller.MovementController
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*

abstract class AbstractFlyingEntity protected constructor(type: EntityType<out FlyingEntity>, worldIn: World) : FlyingEntity(type, worldIn)
{
    var orbitOffset = Vec3d.ZERO
    var orbitPosition = BlockPos.ZERO
    var attackPhase = AttackPhases.CIRCLE
}

enum class AttackPhases
{
    CIRCLE, SWOOP
}

class ThreeDimensionsLookHelperController(entityIn: MobEntity) : LookController(entityIn)
{
    /**
     * Updates look
     */
    override fun tick()
    {
    }
}

class ThreeDimensionsMoveHelperController(private val parentEntity: FlyingEntity) : MovementController(parentEntity)
{
    private var courseChangeCooldown = 0
    override fun tick()
    {
        if (action == Action.MOVE_TO)
        {
            if (courseChangeCooldown-- <= 0)
            {
                courseChangeCooldown += parentEntity.rng.nextInt(5) + 2
                var vec3d = Vec3d(posX - parentEntity.posX, posY - parentEntity.posY, posZ - parentEntity.posZ)
                val d0 = vec3d.length()
                vec3d = vec3d.normalize()
                if (func_220673_a(vec3d, MathHelper.ceil(d0)))
                {
                    parentEntity.motion = parentEntity.motion.add(vec3d.scale(0.1))
                } else
                {
                    action = Action.WAIT
                }
            }
        }
    }

    private fun func_220673_a(p_220673_1_: Vec3d, p_220673_2_: Int): Boolean
    {
        var axisalignedbb = parentEntity.boundingBox
        for (i in 1 until p_220673_2_)
        {
            axisalignedbb = axisalignedbb.offset(p_220673_1_)
            if (!parentEntity.world.hasNoCollisions(parentEntity, axisalignedbb))
            {
                return false
            }
        }
        return true
    }
}


abstract class ThreeDimensionsMovingGoal(entityIn: AbstractFlyingEntity) : Goal()
{
    protected var entity: AbstractFlyingEntity
    protected val isNear: Boolean
        get() = entity.orbitOffset.squareDistanceTo(entity.posX, entity.posY, entity.posZ) < 4.0

    init
    {
        this.mutexFlags = EnumSet.of(Flag.MOVE)
        entity = entityIn
    }
}