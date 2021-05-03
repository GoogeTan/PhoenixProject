package phoenix.enity.boss.phase.phases.redo

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.pathfinding.PathPoint
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.AshHoldingPatternPhase

class RedoHoldingPatternPhase(dragonIn: AbstractEnderDragonEntity) : AshHoldingPatternPhase(dragonIn)
{
    override val type: PhaseType = PhaseType.REDO_HOLDING_PATTERN

    override fun strafePlayer(player: PlayerEntity)
    {
        dragon.phaseManager.setPhase(PhaseType.REDO_STRAFE_PLAYER)
        dragon.phaseManager.getPhase<RedoStrafePlayerPhase>(PhaseType.REDO_STRAFE_PLAYER)?.setTarget(player)
    }

    override fun findNewTarget()
    {
        if (currentPath != null && currentPath!!.isFinished)
        {
            val topPos = dragon.world!!.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BlockPos(EndPodiumFeature.END_PODIUM_LOCATION))
            val aliveCrystals = if (dragon.fightManager == null) 0 else dragon.fightManager!!.numAliveCrystals
            if (dragon.rng.nextInt(aliveCrystals + 3) == 0)
            {
                dragon.phaseManager.setPhase(PhaseType.REDO_LANDING_APPROACH)
                return
            }
            var dist = 64.0
            val player = dragon.world.getClosestPlayer(NEW_TARGET_TARGETING, topPos.x.toDouble(), topPos.y.toDouble(), topPos.z.toDouble())
            if (player != null)
            {
                dist = topPos.distanceSq(player.positionVec, true) / 512.0
            }
            if (player != null && !player.abilities.disableDamage && (dragon.rng.nextInt(MathHelper.abs(dist.toInt()) + 2) == 0 || dragon.rng.nextInt(aliveCrystals + 2) == 0))
            {
                strafePlayer(player)
                return
            }
        }
        if (currentPath == null || currentPath!!.isFinished)
        {
            val path = dragon.initPathPoints()
            var tmp = path
            if (dragon.rng.nextInt(8) == 0)
            {
                clockwise = !clockwise
                tmp = path + 6
            }
            if (clockwise)
            {
                ++tmp
            } else
            {
                --tmp
            }
            if (dragon.fightManager != null && dragon.fightManager!!.numAliveCrystals >= 0)
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
        navigateToNextPathNode()
    }
}