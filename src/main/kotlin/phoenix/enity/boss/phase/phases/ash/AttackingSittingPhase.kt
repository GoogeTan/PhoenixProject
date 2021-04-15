package phoenix.enity.boss.phase.phases.ash

import net.minecraft.util.SoundEvents
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.enity.boss.phase.PhaseType

open class AttackingSittingPhase(dragonIn: AbstractEnderDragonEntity) : SittingPhase(dragonIn)
{
    protected var attackingTicks = 0

    /**
     * Generates particle effects appropriate to the phase (or sometimes sounds).
     * Called by dragon's onLivingUpdate. Only used when worldObj.isRemote.
     */
    override fun clientTick()
    {
        dragon.world.playSound(
            dragon.posX,
            dragon.posY,
            dragon.posZ,
            SoundEvents.ENTITY_ENDER_DRAGON_GROWL,
            dragon.soundCategory,
            2.5f,
            0.8f + dragon.rng.nextFloat() * 0.3f,
            false
        )
    }

    /**
     * Gives the phase a chance to update its status.
     * Called by dragon's onLivingUpdate. Only used when !worldObj.isRemote.
     */
    override fun serverTick()
    {
        if (attackingTicks++ >= 40)
        {
            dragon.phaseManager.setPhase(PhaseType.SITTING_FLAMING)
        }
    }

    /**
     * Called when this phase is set to active
     */
    override fun initPhase()
    {
        attackingTicks = 0
    }

    override val type = PhaseType.SITTING_ATTACKING
}
