package phoenix.enity.boss.phase.phases.redo

import net.minecraft.pathfinding.PathPoint
import net.minecraft.util.math.Vec3d
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.AshLandingApproachPhase

class RedoLandingApproachPhase(dragonIn: AbstractEnderDragonEntity) : AshLandingApproachPhase(dragonIn)
{
    override val type: PhaseType = PhaseType.REDO_LANDING_APPROACH

    override fun findNewTarget()
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
            dragon.phaseManager.setPhase(PhaseType.REDO_LANDING)
        }
    }
}