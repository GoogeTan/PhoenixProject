package phoenix.enity.boss.phase.phases.redo

import net.minecraft.util.math.Vec3d
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.AshFlamingSittingPhase
import phoenix.enity.boss.phase.phases.ash.AshLandingPhase

class RedoLandingPhase(dragonIn: AbstractEnderDragonEntity) : AshLandingPhase(dragonIn)
{
    override val type: PhaseType = PhaseType.REDO_LANDING

    override fun serverTick()
    {
        if (targetLocation == null)
        {
            targetLocation = Vec3d(
                dragon.world.getHeight(
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    EndPodiumFeature.END_PODIUM_LOCATION
                )
            )
        }
        if (targetLocation!!.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ) < 1.0)
        {
            dragon.phaseManager.getPhase<AshFlamingSittingPhase>(PhaseType.REDO_SITTING_FLAMING)?.resetFlameCount()
            dragon.phaseManager.setPhase(PhaseType.REDO_SITTING_SCANNING)
        }
    }
}