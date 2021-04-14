package phoenix.enity.boss.phase.phases.redo

import net.minecraft.entity.player.PlayerEntity
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.HoldingPatternPhase

class RedoHoldingPatternPhase(dragonIn: AbstractEnderDragonEntity) : HoldingPatternPhase(dragonIn)
{
    override fun strafePlayer(player: PlayerEntity)
    {
        dragon.phaseManager.setPhase(PhaseType.REDO_STRAFE_PLAYER)
        dragon.phaseManager.getPhase<RedoStrafePlayerPhase>(PhaseType.REDO_STRAFE_PLAYER)?.setTarget(player)
    }
}