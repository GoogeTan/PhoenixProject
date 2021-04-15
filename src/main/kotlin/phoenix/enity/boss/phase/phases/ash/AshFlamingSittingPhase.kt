package phoenix.enity.boss.phase.phases.ash

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.particles.ParticleTypes
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType

open class AshFlamingSittingPhase(dragonIn: AbstractEnderDragonEntity) : SittingPhase(dragonIn)
{
    protected var flameTicks = 0
    protected var flameCount = 0
    protected var areaEffectCloud: AreaEffectCloudEntity? = null

    /**
     * Generates particle effects appropriate to the phase (or sometimes sounds).
     * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
     */
    override fun clientTick()
    {
        ++flameTicks
        if (flameTicks % 2 == 0 && flameTicks < 10)
        {
            val vec3d = dragon.getHeadLookVec(1.0f).normalize()
            vec3d.rotateYaw((-Math.PI).toFloat() / 4f)
            val d0 = dragon.dragonPartHead.posX
            val d1 = dragon.dragonPartHead.getPosYHeight(0.5)
            val d2 = dragon.dragonPartHead.posZ
            for (i in 0..7)
            {
                val d3 = d0 + dragon.rng.nextGaussian() / 2.0
                val d4 = d1 + dragon.rng.nextGaussian() / 2.0
                val d5 = d2 + dragon.rng.nextGaussian() / 2.0
                for (j in 0..5)
                {
                    dragon.world.addParticle(
                        ParticleTypes.DRAGON_BREATH,
                        d3,
                        d4,
                        d5,
                        -vec3d.x * 0.08f.toDouble() * j.toDouble(),
                        -vec3d.y * 0.6f.toDouble(),
                        -vec3d.z * 0.08f.toDouble() * j.toDouble()
                    )
                }
                vec3d.rotateYaw(0.19634955f)
            }
        }
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        ++flameTicks
        if (flameTicks >= 200)
        {
            if (flameCount >= 4)
            {
                dragon.phaseManager.setPhase(PhaseType.ASH_TAKEOFF)
            } else
            {
                dragon.phaseManager.setPhase(PhaseType.ASH_SITTING_SCANNING)
            }
        } else if (flameTicks == 10)
        {
            val vec3d = Vec3d(dragon.dragonPartHead.posX - dragon.posX, 0.0, dragon.dragonPartHead.posZ - dragon.posZ)
                .normalize()
            val f = 5.0f
            val d0 = dragon.dragonPartHead.posX + vec3d.x * 5.0 / 2.0
            val d1 = dragon.dragonPartHead.posZ + vec3d.z * 5.0 / 2.0
            val d2 = dragon.dragonPartHead.getPosYHeight(0.5)
            var d3 = d2
            val `blockpos$mutable` = BlockPos.Mutable(d0, d2, d1)
            while (dragon.world.isAirBlock(`blockpos$mutable`))
            {
                --d3
                if (d3 < 0.0)
                {
                    d3 = d2
                    break
                }
                `blockpos$mutable`.setPos(d0, d3, d1)
            }
            d3 = (MathHelper.floor(d3) + 1).toDouble()
            areaEffectCloud = AreaEffectCloudEntity(dragon.world, d0, d3, d1)
            areaEffectCloud!!.owner = dragon
            areaEffectCloud!!.radius = 5.0f
            areaEffectCloud!!.duration = 200
            areaEffectCloud!!.particleData = ParticleTypes.DRAGON_BREATH
            areaEffectCloud!!.addEffect(EffectInstance(Effects.INSTANT_DAMAGE))
            dragon.world.addEntity(areaEffectCloud)
        }
    }

    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
        flameTicks = 0
        ++flameCount
    }

    override fun removeAreaEffect()
    {
        if (areaEffectCloud != null)
        {
            areaEffectCloud!!.remove()
            areaEffectCloud = null
        }
    }

    override val type = PhaseType.ASH_SITTING_FLAMING

    fun resetFlameCount()
    {
        flameCount = 0
    }
}
