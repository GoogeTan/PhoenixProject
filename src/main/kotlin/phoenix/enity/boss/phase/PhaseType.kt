package phoenix.enity.boss.phase

import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.phases.ash.*
import phoenix.enity.boss.phase.phases.redo.*
import phoenix.utils.ArrayUtils.resize

class PhaseType private constructor(private val id: Int, private val constructor: (AbstractEnderDragonEntity) -> IPhase, private val name: String)
{
    fun createPhase(dragon: AbstractEnderDragonEntity): IPhase = constructor(dragon)

    fun getId(): Int = id

    override fun toString(): String = "$name (#$id)"

    companion object
    {
        private var phases = ArrayList<PhaseType?>()
        val HOLDING_PATTERN = create(::HoldingPatternPhase, "HoldingPattern")
        val STRAFE_PLAYER = create(::StrafePlayerPhase, "StrafePlayer")
        val LANDING_APPROACH = create(::LandingApproachPhase, "LandingApproach")
        val LANDING = create(::LandingPhase, "Landing")
        val TAKEOFF = create(::TakeoffPhase, "Takeoff")
        val SITTING_FLAMING = create(::FlamingSittingPhase, "SittingFlaming")
        val SITTING_SCANNING = create(::ScanningSittingPhase, "SittingScanning")
        val SITTING_ATTACKING = create(::AttackingSittingPhase, "SittingAttacking")
        val CHARGING_PLAYER = create(::ChargingPlayerPhase, "ChargingPlayer")
        val DYING = create(::DyingPhase, "Dying")
        val HOVER = create(::HoverPhase, "Hover")

        val REDO_STRAFE_PLAYER = create(::RedoStrafePlayerPhase, "RedoStrafePlayer")
        val REDO_HOLDING_PATTERN = create(::RedoHoldingPatternPhase, "RedoHoldingPattern")
        val REDO_CHARGING_PLAYER = create(::RedoChargingPlayerPhase, "RedoChargingPlayer")
        val REDO_TAKEOFF = create(::RedoTakeoffPhase, "RedoTakeoff")
        val REDO_SITTING_FLAMING = create(::RedoFlamingSittingPhase, "SittingFlaming")
        /**
         * Gets a phase by its ID. If the phase is out of bounds (negative or beyond the end of the phase array), returns
         * [.HOLDING_PATTERN].
         */
        fun getById(idIn: Int) = if (idIn >= 0 && idIn < phases.size) phases[idIn] else HOLDING_PATTERN

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
