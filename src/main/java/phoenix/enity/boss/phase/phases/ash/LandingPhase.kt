package phoenix.enity.boss.phase.phases.ash

import net.minecraft.entity.Entity
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.Phase
import kotlin.math.min


class LandingPhase(dragonIn: AbstractEnderDragonEntity) : Phase(dragonIn)
{
    override var targetLocation: Vec3d? = null
    override val type =  PhaseType.LANDING
    /**
     * Generates particle effects appropriate to the phase (or sometimes sounds).
     * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
     */
    override fun clientTick()
    {
        val vec3d = dragon.getHeadLookVec(1.0f).normalize()
        vec3d.rotateYaw((-Math.PI).toFloat() / 4f)
        val d0 = dragon.dragonPartHead.posX
        val d1 = dragon.dragonPartHead.getPosYHeight(0.5)
        val d2 = dragon.dragonPartHead.posZ
        for (i in 0..7)
        {
            val random = dragon.rng
            val d3 = d0 + random.nextGaussian() / 2.0
            val d4 = d1 + random.nextGaussian() / 2.0
            val d5 = d2 + random.nextGaussian() / 2.0
            val vec3d1 = dragon.motion
            dragon.world.addParticle(
                ParticleTypes.DRAGON_BREATH,
                d3,
                d4,
                d5,
                -vec3d.x * 0.08f.toDouble() + vec3d1.x,
                -vec3d.y * 0.3f.toDouble() + vec3d1.y,
                -vec3d.z * 0.08f.toDouble() + vec3d1.z
            )
            vec3d.rotateYaw(0.19634955f)
        }
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        if (targetLocation == null)
        {
            targetLocation = Vec3d(
                dragon.world.getHeight(
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                    EndPodiumFeature.END_PODIUM_LOCATION
                )
            )
        }
        if (targetLocation!!.squareDistanceTo(dragon.posX, dragon.posY, dragon.posZ) < 1.0)
        {
            dragon.phaseManager.getPhase<FlamingSittingPhase>(PhaseType.SITTING_FLAMING)?.resetFlameCount()
            dragon.phaseManager.setPhase(PhaseType.SITTING_SCANNING)
        }
    }

    /**
     * Returns the maximum amount dragon may rise or fall during this phase
     */

    override val maxRiseOrFall: Float= 1.5f
    override val yawFactor: Float
        get(){
            val f = MathHelper.sqrt(Entity.horizontalMag(dragon.motion)) + 1.0f
            val f1 = min(f, 40.0f)
            return f1 / f
        }



    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
        targetLocation = null
    }
}
