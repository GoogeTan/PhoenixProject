package phoenix.enity.boss.phase.phases.redo

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.particles.ParticleTypes
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType
import phoenix.enity.boss.phase.phases.ash.AshFlamingSittingPhase

class RedoFlamingSittingPhase(dragonIn: AbstractEnderDragonEntity) : AshFlamingSittingPhase(dragonIn)
{
    override val type: PhaseType = PhaseType.REDO_SITTING_FLAMING

    override fun serverTick()
    {
        ++flameTicks
        if (flameTicks >= 200)
        {
            if (flameCount >= 4)
            {
                dragon.phaseManager.setPhase(PhaseType.REDO_TAKEOFF)
            } else
            {
                dragon.phaseManager.setPhase(PhaseType.REDO_SITTING_SCANNING)
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
}