package phoenix.enity.boss.phase.phases.redo

import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.AttackingSittingPhase

class RedoAttackingSittingPhase(dragonIn: AbstractEnderDragonEntity) : AttackingSittingPhase(dragonIn)
{
    override val type: PhaseType = PhaseType.REDO_SITTING_ATTACKING

    override fun serverTick()
    {
        if (attackingTicks++ >= 40)
        {
            dragon.phaseManager.setPhase(PhaseType.REDO_SITTING_FLAMING)
        }
    }

}