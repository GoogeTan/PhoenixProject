package phoenix.enity.boss.phase.phases

import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import phoenix.enity.EnderCrystalEntity
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.IPhase
import kotlin.math.min

abstract class Phase(protected val dragon: AbstractEnderDragonEntity) : IPhase
{

    override val isStationary: Boolean
        get() = false

    /**
     * Generates particle effects appropriate to the phase (or sometimes sounds).
     * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
     */
    override fun clientTick()
    {
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
    }

    override fun onCrystalDestroyed(
        crystal: EnderCrystalEntity,
        pos: BlockPos,
        dmgSrc: DamageSource,
        plyr: PlayerEntity?
    )
    {
    }

    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
    }

    override fun removeAreaEffect()
    {
    }

    /**
     * Returns the maximum amount dragon may rise or fall during this phase
     */
    override val maxRiseOrFall: Float
        get() = 0.6f

    /**
     * Returns the location the dragon is flying toward
     */
    override val targetLocation: Vec3d?
        get() = null

    override fun func_221113_a(source: DamageSource, amount: Float): Float = amount


    override val yawFactor: Float
        get() {
            val f = MathHelper.sqrt(Entity.horizontalMag(dragon.motion)) + 1.0f
            val f1 = min(f, 40.0f)
            return 0.7f / f1 / f
        }
}
