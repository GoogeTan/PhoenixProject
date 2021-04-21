package phoenix.enity.boss.phase.phases.ash

import net.minecraft.pathfinding.Path
import net.minecraft.pathfinding.PathPoint
import net.minecraft.util.math.Vec3d
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.Phase

open class AshTakeoffPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    protected var firstTick = false
    protected var currentPath: Path? = null
    override var targetLocation: Vec3d? = null

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        if (!firstTick && currentPath != null)
        {
            val pos =
                dragon.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION)
            if (!pos.withinDistance(dragon.positionVec, 10.0))
            {
                dragon.phaseManager.setPhase(PhaseType.ASH_HOLDING_PATTERN)
            }
        } else
        {
            firstTick = false
            findNewTarget()
        }
    }

    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
        firstTick = true
        currentPath = null
        targetLocation = null
    }

    protected fun findNewTarget()
    {
        val i = dragon.findClosestNode()
        val vec3d = dragon.getHeadLookVec(1.0f)
        var j = dragon.getNearestPpIdx(-vec3d.x * 40.0, 105.0, -vec3d.z * 40.0)
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
        navigateToNextPathNode()
    }

    private fun navigateToNextPathNode()
    {
        if (currentPath != null)
        {
            currentPath!!.incrementPathIndex()
            if (!currentPath!!.isFinished)
            {
                val vec3d = currentPath!!.currentPos
                currentPath!!.incrementPathIndex()
                var d0: Double
                while (true)
                {
                    d0 = vec3d.y + (dragon.rng.nextFloat() * 20.0f).toDouble()
                    if (d0 >= vec3d.y)
                    {
                        break
                    }
                }
                targetLocation = Vec3d(vec3d.x, d0, vec3d.z)
            }
        }
    }

    override val type = PhaseType.ASH_TAKEOFF
}
