package phoenix.enity.boss.phase.phases.ash

import net.minecraft.particles.ParticleTypes
import net.minecraft.util.math.Vec3d
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.Phase


class DyingPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    override var targetLocation: Vec3d? = null
    private var time = 0

    /**
     * Generates particle effects appropriate to the phase (or sometimes sounds).
     * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
     */
    override fun clientTick()
    {
        if (time++ % 10 == 0)
        {
            val f = (dragon.rng.nextFloat() - 0.5f) * 8.0f
            val f1 = (dragon.rng.nextFloat() - 0.5f) * 4.0f
            val f2 = (dragon.rng.nextFloat() - 0.5f) * 8.0f
            dragon.world.addParticle(
                ParticleTypes.EXPLOSION_EMITTER,
                dragon.posX + f.toDouble(),
                dragon.posY + 2.0 + f1.toDouble(),
                dragon.posZ + f2.toDouble(),
                0.0,
                0.0,
                0.0
            )
        }
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        ++time
        if (targetLocation == null)
        {
            val pos = dragon.world.getHeight(Heightmap.Type.MOTION_BLOCKING, EndPodiumFeature.END_PODIUM_LOCATION)
            targetLocation = Vec3d(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
        }
        val dist = targetLocation!!.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ)
        dragon.health = if (dist in 100.0..22500.0 && !dragon.collidedHorizontally && !dragon.collidedVertically) 1.0f else 0.0f
    }

    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
        targetLocation = null
        time = 0
    }

    override val maxRiseOrFall: Float = 3.0f

    override val type: PhaseType = PhaseType.DYING
}
