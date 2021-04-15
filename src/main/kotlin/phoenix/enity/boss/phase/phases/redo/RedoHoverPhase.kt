package phoenix.enity.boss.phase.phases.redo

import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.AshHoverPhase

class RedoHoverPhase(dragonIn: AbstractEnderDragonEntity) : AshHoverPhase(dragonIn)
{
    override val type: PhaseType = PhaseType.REDO_HOVER
}