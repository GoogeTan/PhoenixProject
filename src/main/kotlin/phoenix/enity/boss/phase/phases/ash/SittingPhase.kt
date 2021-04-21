package phoenix.enity.boss.phase.phases.ash

import net.minecraft.entity.projectile.AbstractArrowEntity
import net.minecraft.util.DamageSource
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.phases.Phase

abstract class SittingPhase(dragon: AbstractEnderDragonEntity) : Phase(dragon)
{
    override val isStationary = true

    override fun onHurt(source: DamageSource, amount: Float): Float
    {
        val s = source.immediateSource
        return if (s is AbstractArrowEntity)
        {
            s.setFire(1)
            0.0f
        } else
        {
            super.onHurt(source, amount)
        }
    }
}
