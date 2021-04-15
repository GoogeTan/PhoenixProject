package phoenix.enity.boss.phase.phases.ash

import net.minecraft.entity.EntityPredicate
import net.minecraft.pathfinding.Path
import net.minecraft.pathfinding.PathPoint
import net.minecraft.util.math.Vec3d
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.Phase

open class AshLandingApproachPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    protected var currentPath: Path? = null
    override var targetLocation: Vec3d? = null
    override val type =  PhaseType.ASH_LANDING_APPROACH

    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
        currentPath = null
        targetLocation = null
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        val dist = if (targetLocation == null) 0.0 else targetLocation!!.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ)
        if (dist < 100.0 || dist > 22500.0 || dragon.collidedHorizontally || dragon.collidedVertically)
        {
            findNewTarget()
        }
    }

    protected open fun findNewTarget()
    {
        if (currentPath == null || currentPath!!.isFinished)
        {
            val i = dragon.initPathPoints()
            val blockpos =
                dragon.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION)
            val playerentity = dragon.world.getClosestPlayer(
                field_221118_b,
                blockpos.x.toDouble(), blockpos.y.toDouble(), blockpos.z.toDouble()
            )
            val j: Int = if (playerentity != null)
            {
                val vec3d = Vec3d(playerentity.posX, 0.0, playerentity.posZ).normalize()
                dragon.getNearestPpIdx(-vec3d.x * 40.0, 105.0, -vec3d.z * 40.0)
            } else
            {
                dragon.getNearestPpIdx(40.0, blockpos.y.toDouble(), 0.0)
            }
            val pathpoint = PathPoint(blockpos.x, blockpos.y, blockpos.z)
            currentPath = dragon.findPath(i, j, pathpoint)
            if (currentPath != null)
            {
                currentPath!!.incrementPathIndex()
            }
        }
        navigateToNextPathNode()
        if (currentPath != null && currentPath!!.isFinished)
        {
            dragon.phaseManager.setPhase(PhaseType.ASH_LANDING)
        }
    }

    protected fun navigateToNextPathNode()
    {
        if (currentPath != null && !currentPath!!.isFinished)
        {
            val vec3d = currentPath!!.currentPos
            currentPath!!.incrementPathIndex()
            val d0 = vec3d.x
            val d1 = vec3d.z
            var d2: Double
            while (true)
            {
                d2 = vec3d.y + (dragon.rng.nextFloat() * 20.0f).toDouble()
                if (d2 >= vec3d.y)
                {
                    break
                }
            }
            targetLocation = Vec3d(d0, d2, d1)
        }
    }

    companion object
    {
        val field_221118_b = EntityPredicate().setDistance(128.0)
    }
}
