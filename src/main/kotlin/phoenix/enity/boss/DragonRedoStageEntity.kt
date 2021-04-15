package phoenix.enity.boss

import net.minecraft.entity.EntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.gen.Heightmap
import net.minecraft.world.gen.feature.EndPodiumFeature
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import phoenix.enity.boss.phase.PhaseType
import kotlin.math.max
import kotlin.math.min

class DragonRedoStageEntity(type: EntityType<out DragonRedoStageEntity>, worldIn: World) : AbstractEnderDragonEntity(type, worldIn)
{
    @OnlyIn(Dist.CLIENT)
    override fun getHeadPartYOffset(p_184667_1_: Int, p_184667_2_: DoubleArray, p_184667_3_: DoubleArray): Float
    {
        val iphase = phaseManager.currentPhase
        val phasetype = iphase?.type
        val d0: Double = if (phasetype !== PhaseType.REDO_LANDING && phasetype !== PhaseType.REDO_TAKEOFF)
        {
            when
            {
                iphase?.isStationary?:false ->
                {
                    p_184667_1_.toDouble()
                }
                p_184667_1_ == 6     ->
                {
                    0.0
                }
                else                 ->
                {
                    p_184667_3_[1] - p_184667_2_[1]
                }
            }
        } else
        {
            val blockpos =
                world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION)
            val f = max(MathHelper.sqrt(blockpos.distanceSq(this.positionVec, true)) / 4.0f, 1.0f)
            (p_184667_1_.toFloat() / f).toDouble()
        }
        return d0.toFloat()
    }

    override fun getHeadLookVec(p_184665_1_: Float): Vec3d
    {
        val iphase = phaseManager.currentPhase
        val phasetype = iphase!!.type
        val vec3d: Vec3d
        if (phasetype !== PhaseType.REDO_LANDING && phasetype !== PhaseType.REDO_TAKEOFF)
        {
            if (iphase.isStationary)
            {
                val f4 = rotationPitch
                val f5 = 1.5f
                rotationPitch = -45.0f
                vec3d = getLook(p_184665_1_)
                rotationPitch = f4
            } else
            {
                vec3d = getLook(p_184665_1_)
            }
        } else
        {
            val blockpos =
                world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPodiumFeature.END_PODIUM_LOCATION)
            val f = max(MathHelper.sqrt(blockpos.distanceSq(this.positionVec, true)) / 4.0f, 1.0f)
            val f1 = 6.0f / f
            val f2 = rotationPitch
            val f3 = 1.5f
            rotationPitch = -f1 * 1.5f * 5.0f
            vec3d = getLook(p_184665_1_)
            rotationPitch = f2
        }
        return vec3d
    }

    override fun attackPart(part: AbstractDragonPartEntity, source: DamageSource, damageIn: Float): Boolean
    {
        var damage = damageIn
        return if (phaseManager.currentPhase!!.type === PhaseType.REDO_DYING)
        {
            false
        } else
        {
            damage = phaseManager.currentPhase!!.func_221113_a(source, damage)
            if (part !== dragonPartHead)
            {
                damage = damage / 4.0f + min(damage, 1.0f)
            }
            if (damage < 0.01f)
            {
                false
            } else
            {
                if (source.trueSource is PlayerEntity || source.isExplosion)
                {
                    val f = this.health
                    attackDragonFrom(source, damage)
                    if (this.health <= 0.0f && !phaseManager.currentPhase!!.isStationary)
                    {
                        this.health = 1.0f
                        phaseManager.setPhase(PhaseType.REDO_DYING)
                    }
                    if (phaseManager.currentPhase!!.isStationary)
                    {
                        sittingDamageReceived = (sittingDamageReceived.toFloat() + (f - this.health)).toInt()
                        if (sittingDamageReceived.toFloat() > 0.25f * this.maxHealth)
                        {
                            sittingDamageReceived = 0
                            phaseManager.setPhase(PhaseType.REDO_TAKEOFF)
                        }
                    }
                }
                true
            }
        }
    }
}
