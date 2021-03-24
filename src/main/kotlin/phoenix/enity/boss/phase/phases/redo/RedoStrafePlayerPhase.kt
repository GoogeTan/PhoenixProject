package phoenix.enity.boss.phase.phases.redo

import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.StrafePlayerPhase

open class RedoStrafePlayerPhase(dragon: AbstractEnderDragonEntity) : StrafePlayerPhase(dragon)
{
    override val type: PhaseType = PhaseType.REDO_STRAFE_PLAYER
}