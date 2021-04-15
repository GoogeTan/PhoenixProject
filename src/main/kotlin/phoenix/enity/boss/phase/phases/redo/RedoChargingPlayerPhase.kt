package phoenix.enity.boss.phase.phases.redo

import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.ChargingPlayerPhase

class RedoChargingPlayerPhase(dragonIn: AbstractEnderDragonEntity) : ChargingPlayerPhase(dragonIn)
{
    override fun serverTick()
    {
        if (targetLocation == null)
        {
            LOGGER.warn("Aborting charge player as no target was set.")
            dragon.phaseManager.setPhase(PhaseType.REDO_HOLDING_PATTERN)
        }
        else if (timeSinceCharge > 0 && timeSinceCharge++ >= 10)
        {
            dragon.phaseManager.setPhase(PhaseType.REDO_HOLDING_PATTERN)
        }
        else
        {
            val d0 = targetLocation!!.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ)
            if (d0 < 100.0 || d0 > 22500.0 || dragon.collidedHorizontally || dragon.collidedVertically)
            {
                ++timeSinceCharge
            }

        }
    }

    override val type: PhaseType = PhaseType.REDO_CHARGING_PLAYER
}