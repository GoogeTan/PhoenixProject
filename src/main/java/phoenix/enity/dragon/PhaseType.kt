package phoenix.enity.dragon

import net.minecraft.entity.boss.dragon.phase.*
import java.lang.Error
import java.lang.reflect.Constructor
import java.util.*


class PhaseType<T : IPhase> private constructor(
    val id: Int,
    private val clazz: Class<out IPhase>,
    private val name: String
)
{
    fun createPhase(dragon: EnderDragonEntity): IPhase
    {
        return try
        {
            val constructor = getConstructor()
            constructor.newInstance(dragon)
        } catch (exception: Exception)
        {
            throw Error(exception)
        }
    }

    @Throws(NoSuchMethodException::class)
    protected fun getConstructor(): Constructor<out IPhase>
    {
        return clazz.getConstructor(EnderDragonEntity::class.java)
    }

    @JvmName("getId1")
    fun getId() = id

    override fun toString(): String
    {
        return "$name (#$id)"
    }

    companion object
    {
        private var phases: Array<PhaseType<*>?> = arrayOfNulls(0)
        val HOLDING_PATTERN = create(
            HoldingPatternPhase::class.java, "HoldingPattern"
        )
        val STRAFE_PLAYER = create(
            StrafePlayerPhase::class.java, "StrafePlayer"
        )
        val LANDING_APPROACH = create(
            LandingApproachPhase::class.java, "LandingApproach"
        )
        val LANDING = create(LandingPhase::class.java, "Landing")
        val TAKEOFF = create(TakeoffPhase::class.java, "Takeoff")
        val SITTING_FLAMING = create(
            FlamingSittingPhase::class.java, "SittingFlaming"
        )
        val SITTING_SCANNING = create(
            ScanningSittingPhase::class.java, "SittingScanning"
        )
        val SITTING_ATTACKING = create(
            AttackingSittingPhase::class.java, "SittingAttacking"
        )
        val CHARGING_PLAYER = create(
            ChargingPlayerPhase::class.java, "ChargingPlayer"
        )
        val DYING = create(DyingPhase::class.java, "Dying")
        val HOVER = create(HoverPhase::class.java, "Hover")

        /**
         * Gets a phase by its ID. If the phase is out of bounds (negative or beyond the end of the phase array), returns
         * [.HOLDING_PATTERN].
         */
        fun getById(idIn: Int): PhaseType<*> = if (idIn >= 0 && idIn < phases.size) phases[idIn]!! else HOLDING_PATTERN

        fun getTotalPhases() = phases.size

        private fun <T : IPhase> create(phaseIn: Class<T>, nameIn: String): PhaseType<T>
        {
            val phasetype: PhaseType<T> = PhaseType(phases.size, phaseIn, nameIn)
            phases = Arrays.copyOf(phases, phases.size + 1)
            phases[phasetype.getId()] = phasetype
            return phasetype
        }
    }
}
