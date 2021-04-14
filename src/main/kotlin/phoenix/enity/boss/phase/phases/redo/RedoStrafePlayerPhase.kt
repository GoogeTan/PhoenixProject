package phoenix.enity.boss.phase.phases.redo

import net.minecraft.entity.projectile.DragonFireballEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.balls.ExplosiveBallEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.StrafePlayerPhase
import phoenix.init.events.PhoenixEvents
import kotlin.math.acos
import kotlin.math.min

open class RedoStrafePlayerPhase(dragon: AbstractEnderDragonEntity) : StrafePlayerPhase(dragon)
{
    override val type: PhaseType = PhaseType.REDO_STRAFE_PLAYER

    override fun serverTick()
    {
        if (attackTarget == null)
        {
            LOGGER.warn("Skipping player strafe phase because no player was found")
            dragon.phaseManager.setPhase(PhaseType.REDO_HOLDING_PATTERN)
        } else
        {
            if (currentPath != null && currentPath!!.isFinished)
            {
                val posX = attackTarget!!.posX
                val posZ = attackTarget!!.posZ
                val deltaX = posX - dragon.posX
                val deltaZ = posZ - dragon.posZ
                val dist = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ).toDouble()
                val deltaY = min(0.4f.toDouble() + dist / 80.0 - 1.0, 10.0)
                targetLocation = Vec3d(posX, attackTarget!!.posY + deltaY, posZ)
            }
            val dist = if (targetLocation == null) 0.0 else targetLocation!!.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ)
            if (dist < 100.0 || dist > 22500.0)
            {
                findNewTarget()
            }
            if (attackTarget!!.getDistanceSq(dragon) < 4096.0)
            {
                if (dragon.canEntityBeSeen(attackTarget!!))
                {
                    ++fireballCharge
                    val vec3d1 = Vec3d(attackTarget!!.posX - dragon.posX, 0.0, attackTarget!!.posZ - dragon.posZ).normalize()
                    val vec3d = Vec3d(
                            MathHelper.sin(dragon.rotationYaw * (Math.PI.toFloat() / 180f))
                                    .toDouble(),
                            0.0,
                            (-MathHelper.cos(dragon.rotationYaw * (Math.PI.toFloat() / 180f))).toDouble()).normalize()
                    val f1 = vec3d.dotProduct(vec3d1).toFloat()
                    var f = (acos(f1.toDouble()) * (180f / Math.PI.toFloat()).toDouble()).toFloat()
                    f += 0.5f
                    if (fireballCharge >= 5 && f >= 0.0f && f < 10.0f)
                    {
                        val look = dragon.getLook(1.0f)
                        val deltaHeadX = dragon.dragonPartHead.posX - look.x
                        val deltaHeadY = dragon.dragonPartHead.getPosYHeight(0.5) + 0.5
                        val deltaHeadZ = dragon.dragonPartHead.posZ - look.z
                        val deltaX = attackTarget!!.posX - deltaHeadX
                        val deltaY = attackTarget!!.getPosYHeight(0.5) - deltaHeadY
                        val deltaZ = attackTarget!!.posZ - deltaHeadZ
                        for (i in 0..2)
                        {
                            PhoenixEvents.addTask(i * 20)
                            {
                                dragon.world.playEvent(null, 1017, BlockPos(dragon), 0)
                                val fireball = if (dragon.world.rand.nextBoolean())
                                    DragonFireballEntity(dragon.world, dragon, deltaX, deltaY, deltaZ)
                                else
                                    ExplosiveBallEntity(dragon.world, dragon, deltaX, deltaY, deltaZ)
                                fireball.setLocationAndAngles(deltaHeadX, deltaHeadY, deltaHeadZ, 0.0f, 0.0f)
                                dragon.world.addEntity(fireball)
                            }
                        }
                        fireballCharge = 0
                        if (currentPath != null)
                        {
                            while (!currentPath!!.isFinished)
                            {
                                currentPath!!.incrementPathIndex()
                            }
                        }
                        dragon.phaseManager.setPhase(PhaseType.REDO_HOLDING_PATTERN)
                    }
                } else if (fireballCharge > 0)
                {
                    --fireballCharge
                }
            } else if (fireballCharge > 0)
            {
                --fireballCharge
            }
        }
    }
}