package phoenix.enity.boss.phase.phases.redo

import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.AshTakeoffPhase

class RedoTakeoffPhase(dragonIn: AbstractEnderDragonEntity) : AshTakeoffPhase(dragonIn)
{
    override val type: PhaseType = PhaseType.REDO_TAKEOFF

    override fun serverTick()
    {
        if (!firstTick && currentPath != null)
        {
            val pos =
                    dragon.world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION)
            if (!pos.withinDistance(dragon.positionVec, 10.0))
            {
                dragon.phaseManager.setPhase(PhaseType.REDO_HOLDING_PATTERN)
            }
        } else
        {
            firstTick = false
            findNewTarget()
        }
    }
}