package phoenix.enity.boss.phase.phases.redo

import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.DyingPhase

class RedoDyingPhase(dragonIn: AbstractEnderDragonEntity) : DyingPhase(dragonIn)
{
    override val type: PhaseType = PhaseType.REDO_DYING
}