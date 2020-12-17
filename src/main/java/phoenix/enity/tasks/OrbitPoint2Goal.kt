package phoenix.enity.tasks

import net.minecraft.block.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import phoenix.init.PhoenixBlocks
import phoenix.utils.entity.AbstractFlyingEntity
import phoenix.utils.entity.AttackPhases
import phoenix.utils.entity.ThreeDimensionsMovingGoal

class OrbitPoint2Goal(var entity: AbstractFlyingEntity) : ThreeDimensionsMovingGoal(entity)
{
    var distance = 10.0
    private var speed = -1f
    private var posX = 0f
    private var posY = 0f
    private var posZ = 0f

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
        if (speed == -1f) speed = entity.rng.nextFloat() * 2.0f * Math.PI.toFloat()
        for (current in BlockPos.getAllInBoxMutable(
                MathHelper.floor(entity.posX - distance),
                MathHelper.floor(entity.posY - distance),
                MathHelper.floor(entity.posZ - distance),
                MathHelper.floor(entity.posX + distance),
                MathHelper.floor(entity.posY + distance),
                MathHelper.floor(entity.posZ + distance)
        ))
        {
            if (entity.world.getBlockState(current).block === Blocks.CHORUS_FLOWER || entity.world.getBlockState(current).block === PhoenixBlocks.FERTILE_END_STONE.get())
            {
                entity.orbitPosition = current
                posX = current.x.toFloat()
                posY = current.y.toFloat()
                posZ = current.z.toFloat()
                updateOffset()
            }
        }
    }

    private fun updateOffset()
    {
        if (BlockPos.ZERO == entity.orbitPosition)
        {
            entity.orbitPosition = BlockPos(entity)
        }
        speed += posZ * 15.0f * (Math.PI.toFloat() / 180f)
        entity.orbitOffset = Vec3d(entity.orbitPosition).add(posX * MathHelper.cos(speed).toDouble(), -4.0f + posY.toDouble(), posX * MathHelper.sin(speed).toDouble())
    }
}
