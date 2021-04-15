package phoenix.enity.boss.phase

import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.phases.ash.*
import phoenix.enity.boss.phase.phases.redo.*
import phoenix.utils.ArrayUtils.resize
import phoenix.world.StageManager

class PhaseType private constructor
    (
        private val id: Int,
        private val constructor: (AbstractEnderDragonEntity) -> IPhase,
        private val name: String
    )
{
    fun createPhase(dragon: AbstractEnderDragonEntity): IPhase = constructor(dragon)

    fun getId(): Int = id

    override fun toString(): String = "$name (#$id)"

    companion object
    {
        private var phases = ArrayList<PhaseType?>()

        val ASH_HOLDING_PATTERN   = create(::AshHoldingPatternPhase, "AshHoldingPattern")
        val ASH_STRAFE_PLAYER     = create(::AshStrafePlayerPhase, "AshStrafePlayer")
        val ASH_LANDING_APPROACH  = create(::AshLandingApproachPhase, "AshLandingApproach")
        val ASH_LANDING           = create(::AshLandingPhase, "AshLanding")
        val ASH_TAKEOFF           = create(::AshTakeoffPhase, "AshTakeoff")
        val ASH_SITTING_FLAMING   = create(::AshFlamingSittingPhase, "AshSittingFlaming")
        val ASH_SITTING_SCANNING  = create(::AshScanningSittingPhase, "AshSittingScanning")
        val ASH_SITTING_ATTACKING = create(::AshAttackingSittingPhase, "AshSittingAttacking")
        val ASH_CHARGING_PLAYER   = create(::AshChargingPlayerPhase, "AshChargingPlayer")
        val ASH_DYING             = create(::AshDyingPhase, "AshDying")
        val ASH_HOVER             = create(::AshHoverPhase, "AshHover")

        val REDO_HOLDING_PATTERN   = create(::RedoHoldingPatternPhase, "RedoHoldingPattern")
        val REDO_STRAFE_PLAYER     = create(::RedoStrafePlayerPhase, "RedoStrafePlayer")
        val REDO_LANDING_APPROACH  = create(::RedoLandingApproachPhase, "RedoLandingApproach")
        val REDO_LANDING           = create(::RedoLandingPhase, "RedoLanding")
        val REDO_TAKEOFF           = create(::RedoTakeoffPhase, "RedoTakeoff")
        val REDO_SITTING_FLAMING   = create(::RedoFlamingSittingPhase, "RedoSittingFlaming")
        val REDO_SITTING_SCANNING  = create(::RedoScanningSittingPhase, "RedoSittingScanning")
        val REDO_SITTING_ATTACKING = create(::RedoAttackingSittingPhase, "RedoSittingAttacking")
        val REDO_CHARGING_PLAYER   = create(::RedoChargingPlayerPhase, "RedoChargingPlayer")
        val REDO_DYING             = create(::RedoDyingPhase, "RedoDying")
        val REDO_HOVER             = create(::RedoHoverPhase, "RedoHover")

        fun getById(idIn: Int) = if (idIn >= 0 && idIn < phases.size) phases[idIn] else StageManager.stageEnum.holdingPhase

        fun getTotalPhases(): Int = phases.size

        private fun create(constructor: (AbstractEnderDragonEntity) -> IPhase, nameIn: String): PhaseType
        {
            val type = PhaseType(phases.size, constructor, nameIn)
            phases.resize(phases.size + 1, null)
            phases[type.getId()] = type
            return type
        }
    }
}
