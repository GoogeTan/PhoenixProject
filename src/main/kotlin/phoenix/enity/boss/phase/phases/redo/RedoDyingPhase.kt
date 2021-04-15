package phoenix.enity.boss.phase.phases.redo

import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.AshDyingPhase

class RedoDyingPhase(dragonIn: AbstractEnderDragonEntity) : AshDyingPhase(dragonIn)
{
    override val type: PhaseType = PhaseType.REDO_DYING
}