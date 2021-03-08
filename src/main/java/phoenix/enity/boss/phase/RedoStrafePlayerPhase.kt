package phoenix.enity.boss.phase

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.entity.boss.dragon.phase.Phase
import net.minecraft.entity.boss.dragon.phase.PhaseType
import net.minecraft.entity.boss.dragon.phase.StrafePlayerPhase
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.DragonFireballEntity
import net.minecraft.pathfinding.Path
import net.minecraft.pathfinding.PathPoint
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import org.apache.logging.log4j.LogManager
import kotlin.math.acos
import kotlin.math.min

open class RedoStrafePlayerPhase(dragon: EnderDragonEntity) : Phase(dragon)
{
    private var fireballCharge = 0
    private var currentPath: Path? = null
    private var targetLocation: Vec3d? = null
    private var attackTarget: LivingEntity? = null
    private var holdingPatternClockwise = false

    override fun serverTick()
    {
        val target = attackTarget
        if (target != null)
        {
            var targetX: Double
            var targetZ: Double
            var dist: Double
            if (currentPath != null && currentPath!!.isFinished)
            {
                targetX = target.posX
                targetZ = target.posZ
                val deltaX = targetX - dragon.posX
                val deltaZ = targetZ - dragon.posZ
                dist = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ).toDouble()
                val deltaY = min(0.4000000059604645 + dist / 80.0 - 1.0, 10.0)
                targetLocation = Vec3d(targetX, target.posY + deltaY, targetZ)
            }
            targetX = if (targetLocation != null) targetLocation!!.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ) else 0.0
            if (targetX < 100.0 || targetX > 22500.0)
            {
                findNewTarget()
            }
            targetZ = 64.0
            if (target.getDistanceSq(dragon) < 4096.0)
            {
                if (dragon.canEntityBeSeen(target))
                {
                    ++fireballCharge
                    val direction  = Vec3d(target.posX - dragon.posX, 0.0, target.posZ - dragon.posZ).normalize()
                    val look = Vec3d(MathHelper.sin(dragon.rotationYaw * 0.017453292f).toDouble(), 0.0, (-MathHelper.cos(dragon.rotationYaw * 0.017453292f)).toDouble()).normalize()
                    val dotedLook = look.dotProduct(direction).toFloat()
                    var lookDist = (acos(dotedLook.toDouble()) * 57.2957763671875).toFloat()
                    lookDist += 0.5f
                    if (fireballCharge >= 5 && lookDist >= 0.0f && lookDist < 10.0f)
                    {
                        dist = 1.0
                        val dragonLook = dragon.getLook(1.0f)
                        val headX = dragon.dragonPartHead.posX - dragonLook.x * 1.0
                        val headY = dragon.dragonPartHead.getPosYHeight(0.5) + 0.5
                        val headZ = dragon.dragonPartHead.posZ - dragonLook.z * 1.0
                        val deltaX = target.posX - headX
                        val deltaY = target.getPosYHeight(0.5) - headY
                        val deltaZ = target.posZ - headZ
                        dragon.world.playEvent(null as PlayerEntity?, 1017, BlockPos(dragon), 0)
                        val fireball = DragonFireballEntity(dragon.world, dragon, deltaX, deltaY, deltaZ)
                        fireball.setLocationAndAngles(headX, headY, headZ, 0.0f, 0.0f)
                        dragon.world.addEntity(fireball)
                        fireballCharge = 0
                        if (currentPath != null)
                        {
                            while (!currentPath!!.isFinished)
                            {
                                currentPath!!.incrementPathIndex()
                            }
                        }
                        dragon.phaseManager.setPhase(PhaseType.HOLDING_PATTERN)
                    }
                } else if (fireballCharge > 0)
                {
                    --fireballCharge
                }
            } else if (fireballCharge > 0)
            {
                --fireballCharge
            }
        } else
        {
            LOGGER.warn("Skipping player strafe phase because no player was found")
            dragon.phaseManager.setPhase(PhaseType.HOLDING_PATTERN)
        }
    }

    private fun findNewTarget()
    {
        if (currentPath != null)
        {
            if(currentPath!!.isFinished)
            {
                val path = dragon.initPathPoints()
                var tmp = path
                if (dragon.rng.nextInt(8) == 0)
                {
                    holdingPatternClockwise = !holdingPatternClockwise
                    tmp = path + 6
                }
                if (holdingPatternClockwise)
                {
                    ++tmp
                } else
                {
                    --tmp
                }
                if (dragon.fightManager != null && dragon.fightManager!!.numAliveCrystals > 0)
                {
                    tmp %= 12
                    if (tmp < 0)
                    {
                        tmp += 12
                    }
                } else
                {
                    tmp -= 12
                    tmp = tmp and 7
                    tmp += 12
                }
                currentPath = dragon.findPath(path, tmp, null as PathPoint?)
                if (currentPath != null)
                {
                    currentPath!!.incrementPathIndex()
                }
            }
        }
        navigateToNextPathNode()
    }

    private fun navigateToNextPathNode()
    {
        if (currentPath != null && !currentPath!!.isFinished)
        {
            val pathPos = currentPath!!.currentPos
            currentPath!!.incrementPathIndex()
            val x = pathPos.x
            val z = pathPos.z
            var y: Double
            do
            {
                y = pathPos.y + (dragon.rng.nextFloat() * 20.0f).toDouble()
            } while (y < pathPos.y)
            targetLocation = Vec3d(x, y, z)
        }
    }

    override fun initPhase()
    {
        fireballCharge = 0
        targetLocation = null
        currentPath = null
        attackTarget = null
    }

    fun setTarget(entity: LivingEntity)
    {
        attackTarget = entity
        val path = dragon.initPathPoints()
        val id = dragon.getNearestPpIdx(attackTarget!!.posX, attackTarget!!.posY, attackTarget!!.posZ)
        val x = MathHelper.floor(attackTarget!!.posX)
        val z = MathHelper.floor(attackTarget!!.posZ)
        val deltaX = x.toDouble() - dragon.posX
        val deltaZ = z.toDouble() - dragon.posZ
        val dist = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ).toDouble()
        val deltaY = min(0.4000000059604645 + dist / 80.0 - 1.0, 10.0)
        val y = MathHelper.floor(attackTarget!!.posY + deltaY)
        val point = PathPoint(x, y, z)
        currentPath = dragon.findPath(path, id, point)
        if (currentPath != null)
        {
            currentPath!!.incrementPathIndex()
            navigateToNextPathNode()
        }
    }

    override fun getTargetLocation(): Vec3d? = targetLocation

    override fun getType(): PhaseType<StrafePlayerPhase> = PhaseType.STRAFE_PLAYER

    companion object
    {
        private val LOGGER = LogManager.getLogger()
    }
}