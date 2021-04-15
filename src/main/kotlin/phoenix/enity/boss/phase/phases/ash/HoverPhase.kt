package phoenix.enity.boss.phase.phases.ash

import net.minecraft.util.math.Vec3d
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.Phase

open class HoverPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    override var targetLocation: Vec3d? = null

    override fun serverTick()
    {
        if (targetLocation == null)
        {
            targetLocation = dragon.positionVec
        }
    }

    override val isStationary: Boolean = true

    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
        targetLocation = null
    }

    /**
     * Returns the maximum amount dragon may rise or fall during this phase
     */
    override val maxRiseOrFall = 1.0f

    override val type = PhaseType.HOVER
}
