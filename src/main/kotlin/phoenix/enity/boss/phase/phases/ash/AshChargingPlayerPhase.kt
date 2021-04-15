package phoenix.enity.boss.phase.phases.ash

import net.minecraft.util.math.Vec3d
import org.apache.logging.log4j.LogManager
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.Phase


open class AshChargingPlayerPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    override var targetLocation: Vec3d? = null
    protected var timeSinceCharge = 0

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        if (targetLocation == null)
        {
            LOGGER.warn("Aborting charge player as no target was set.")
            dragon.phaseManager.setPhase(PhaseType.ASH_HOLDING_PATTERN)
        } else if (timeSinceCharge > 0 && timeSinceCharge++ >= 10)
        {
            dragon.phaseManager.setPhase(PhaseType.ASH_HOLDING_PATTERN)
        } else
        {
            val d0 = targetLocation!!.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ)
            if (d0 < 100.0 || d0 > 22500.0 || dragon.collidedHorizontally || dragon.collidedVertically)
            {
                ++timeSinceCharge
            }
        }
    }

    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
        targetLocation = null
        timeSinceCharge = 0
    }

    fun setTarget(target: Vec3d?)
    {
        targetLocation = target
    }

    override val maxRiseOrFall: Float
        get() = 3.0f

    override val type = PhaseType.ASH_CHARGING_PLAYER

    companion object
    {
        val LOGGER = LogManager.getLogger()
    }
}
