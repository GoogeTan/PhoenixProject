package phoenix.enity.boss.phase.phases.ash

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.DragonFireballEntity
import net.minecraft.pathfinding.Path
import net.minecraft.pathfinding.PathPoint
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.apache.logging.log4j.LogManager
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.Phase
import kotlin.math.acos
import kotlin.math.min

open class AshStrafePlayerPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    protected var fireballCharge = 0
    protected var currentPath: Path? = null
    override var targetLocation: Vec3d? = null
    protected var attackTarget: LivingEntity? = null
    protected var holdingPatternClockwise = false
    override val type: PhaseType = PhaseType.ASH_STRAFE_PLAYER
    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        if (attackTarget == null)
        {
            LOGGER.warn("Skipping player strafe phase because no player was found")
            dragon.phaseManager.setPhase(PhaseType.ASH_HOLDING_PATTERN)
        } else
        {
            if (currentPath != null && currentPath!!.isFinished)
            {
                val d0 = attackTarget!!.posX
                val d1 = attackTarget!!.posZ
                val d2 = d0 - dragon.posX
                val d3 = d1 - dragon.posZ
                val d4 = MathHelper.sqrt(d2 * d2 + d3 * d3).toDouble()
                val d5 = min(0.4f.toDouble() + d4 / 80.0 - 1.0, 10.0)
                targetLocation = Vec3d(d0, attackTarget!!.posY + d5, d1)
            }
            val dist = if (targetLocation == null) 0.0 else targetLocation!!.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ)
            if (dist < 100.0 || dist > 22500.0)
            {
                findNewTarget()
            }
            val d13 = 64.0
            if (attackTarget!!.getDistanceSq(dragon) < 4096.0)
            {
                if (dragon.canEntityBeSeen(attackTarget!!))
                {
                    ++fireballCharge
                    val vec3d1 = Vec3d(attackTarget!!.posX - dragon.posX, 0.0, attackTarget!!.posZ - dragon.posZ)
                        .normalize()
                    val vec3d = Vec3d(
                        MathHelper.sin(dragon.rotationYaw * (Math.PI.toFloat() / 180f))
                            .toDouble(),
                        0.0,
                        (-MathHelper.cos(dragon.rotationYaw * (Math.PI.toFloat() / 180f))).toDouble()
                    ).normalize()
                    val f1 = vec3d.dotProduct(vec3d1).toFloat()
                    var f = (acos(f1.toDouble()) * (180f / Math.PI.toFloat()).toDouble()).toFloat()
                    f += 0.5f
                    if (fireballCharge >= 5 && f >= 0.0f && f < 10.0f)
                    {
                        val d14 = 1.0
                        val look = dragon.getLook(1.0f)
                        val d6 = dragon.dragonPartHead.posX - look.x * 1.0
                        val d7 = dragon.dragonPartHead.getPosYHeight(0.5) + 0.5
                        val d8 = dragon.dragonPartHead.posZ - look.z * 1.0
                        val d9 = attackTarget!!.posX - d6
                        val d10 = attackTarget!!.getPosYHeight(0.5) - d7
                        val d11 = attackTarget!!.posZ - d8
                        dragon.world.playEvent(null as PlayerEntity?, 1017, BlockPos(dragon), 0)
                        val fireball = DragonFireballEntity(dragon.world, dragon, d9, d10, d11)
                        fireball.setLocationAndAngles(d6, d7, d8, 0.0f, 0.0f)
                        dragon.world.addEntity(fireball)
                        fireballCharge = 0
                        if (currentPath != null)
                        {
                            while (!currentPath!!.isFinished)
                            {
                                currentPath!!.incrementPathIndex()
                            }
                        }
                        dragon.phaseManager.setPhase(PhaseType.ASH_HOLDING_PATTERN)
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

    protected fun findNewTarget()
    {
        if (currentPath == null || currentPath!!.isFinished)
        {
            val i = dragon.initPathPoints()
            var j = i
            if (dragon.rng.nextInt(8) == 0)
            {
                holdingPatternClockwise = !holdingPatternClockwise
                j = i + 6
            }
            if (holdingPatternClockwise)
            {
                ++j
            } else
            {
                --j
            }
            if (dragon.fightManager != null && dragon.fightManager!!.numAliveCrystals > 0)
            {
                j %= 12
                if (j < 0)
                {
                    j += 12
                }
            } else
            {
                j -= 12
                j = j and 7
                j += 12
            }
            currentPath = dragon.findPath(i, j, null as PathPoint?)
            if (currentPath != null)
            {
                currentPath!!.incrementPathIndex()
            }
        }
        navigateToNextPathNode()
    }

    private fun navigateToNextPathNode()
    {
        if (currentPath != null && !currentPath!!.isFinished)
        {
            val vec3d = currentPath!!.currentPos
            currentPath!!.incrementPathIndex()
            val d0 = vec3d.x
            val d2 = vec3d.z
            var d1: Double
            do
                d1 = vec3d.y + (dragon.rng.nextFloat() * 20.0f).toDouble()
            while (d1 < vec3d.y)

            targetLocation = Vec3d(d0, d1, d2)
        }
    }

    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
        fireballCharge = 0
        targetLocation = null
        currentPath = null
        attackTarget = null
    }

    open fun setTarget(targetIn: LivingEntity)
    {
        attackTarget = targetIn
        val i = dragon.initPathPoints()
        val j = dragon.getNearestPpIdx(attackTarget!!.posX, attackTarget!!.posY, attackTarget!!.posZ)
        val k = MathHelper.floor(attackTarget!!.posX)
        val l = MathHelper.floor(attackTarget!!.posZ)
        val d0 = k.toDouble() - dragon.posX
        val d1 = l.toDouble() - dragon.posZ
        val d2 = MathHelper.sqrt(d0 * d0 + d1 * d1).toDouble()
        val d3 = min(0.4f.toDouble() + d2 / 80.0 - 1.0, 10.0)
        val i1 = MathHelper.floor(attackTarget!!.posY + d3)
        val pathpoint = PathPoint(k, i1, l)
        currentPath = dragon.findPath(i, j, pathpoint)
        if (currentPath != null)
        {
            currentPath!!.incrementPathIndex()
            navigateToNextPathNode()
        }
    }

    companion object
    {
        val LOGGER = LogManager.getLogger()
    }
}
