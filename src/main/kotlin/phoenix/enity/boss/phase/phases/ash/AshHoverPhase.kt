package phoenix.enity.boss.phase.phases.ash

import net.minecraft.util.math.Vec3d
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.Phase

open class AshHoverPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
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

    override fun initPhase()
    {
        targetLocation = null
    }

    override val maxRiseOrFall = 1.0f

    override val type = PhaseType.ASH_HOVER
}
