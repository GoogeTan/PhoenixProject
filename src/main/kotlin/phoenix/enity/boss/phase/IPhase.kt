package phoenix.enity.boss.phase

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import phoenix.enity.EnderCrystalEntity

interface IPhase
{
    val isStationary: Boolean

    /**
     * Generates particle effects appropriate to the phase (or sometimes sounds).
     * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
     */
    fun clientTick()

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    fun serverTick()
    fun onCrystalDestroyed(crystal: EnderCrystalEntity, pos: BlockPos, dmgSrc: DamageSource, plyr: PlayerEntity?)

    /**
     * Called when this phase is set to active
     */
    fun initPhase()
    fun removeAreaEffect()

    /**
     * Returns the maximum amount dragon may rise or fall during this phase
     */
    val maxRiseOrFall: Float
    val yawFactor: Float
    val type: PhaseType

    /**
     * Returns the location the dragon is flying toward
     */
    val targetLocation: Vec3d?

    fun func_221113_a(source: DamageSource, amount: Float): Float
}
