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
import phoenix.other.LogManager

open class AshLandingApproachPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    protected var currentPath: Path? = null
    override var targetLocation: Vec3d? = null
    override val type: PhaseType
        get() = PhaseType.ASH_LANDING_APPROACH

    override fun initPhase()
    {
        currentPath = null
        targetLocation = null
    }

    override fun serverTick()
    {
        val d0 = if (targetLocation == null) 0.0 else targetLocation!!.squareDistanceTo(
            dragon.posX,
            dragon.posY,
            dragon.posZ
        )
        if (d0 < 100.0 || d0 > 22500.0 || dragon.collidedHorizontally || dragon.collidedVertically)
        {
            findNewTarget()
            //LogManager.error(this.toString())
        }
    }

    protected open fun findNewTarget()
    {
        if (currentPath == null || currentPath!!.isFinished)
        {
            val node : Int = dragon.initPathPoints()
            val toppos = dragon.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION)
            val player = dragon.world.getClosestPlayer(
                field_221118_b,
                toppos.x.toDouble(), toppos.y.toDouble(), toppos.z.toDouble()
            )
            val nodeId: Int = if (player != null)
            {
                val vec3d = Vec3d(player.posX, 0.0, player.posZ).normalize()
                dragon.getNearestPpIdx(-vec3d.x * 40.0, 105.0, -vec3d.z * 40.0)
            } else
                dragon.getNearestPpIdx(40.0, toppos.y.toDouble(), 0.0)
            val point = PathPoint(toppos.x, toppos.y, toppos.z)
            currentPath = dragon.findPath(node, nodeId, point)
            LogManager.error(currentPath.toString())
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

    protected open fun navigateToNextPathNode()
    {
        if (currentPath != null && !currentPath!!.isFinished)
        {
            val pos = currentPath!!.currentPos
            currentPath!!.incrementPathIndex()
            val x = pos.x
            val z = pos.z
            var y: Double
            while (true)
            {
                y = pos.y + (dragon.rng.nextFloat() * 20.0f).toDouble()
                if (y >= pos.y) break
            }
            targetLocation = Vec3d(x, y, z)
        }
    }

    override fun toString(): String = "AshLandingApproachPhase(currentPath=$currentPath, targetLocation=$targetLocation)"

    companion object
    {
        val field_221118_b = EntityPredicate().setDistance(128.0)
    }
}

