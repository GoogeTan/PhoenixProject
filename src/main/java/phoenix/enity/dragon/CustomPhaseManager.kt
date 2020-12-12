package phoenix.enity.dragon

import net.minecraft.entity.boss.dragon.phase.IPhase
import org.apache.logging.log4j.LogManager
import phoenix.enity.dragon.PhaseType

class CustomPhaseManager(private val dragon: EnderDragonEntity)
{
    private val phases = arrayOfNulls<IPhase>(PhaseType.getTotalPhases())
    var currentPhase: IPhase? = null
        private set

    fun setPhase(phaseIn: PhaseType<*>)
    {
        if (currentPhase == null || phaseIn !== currentPhase!!.type)
        {
            if (currentPhase != null)
            {
                currentPhase!!.removeAreaEffect()
            }
            currentPhase = getPhase(phaseIn)
            if (!dragon.world.isRemote)
            {
                dragon.dataManager.set(EnderDragonEntity.PHASE, phaseIn.id)
            }
            LOGGER.debug(
                "Dragon is now in phase {} on the {}",
                phaseIn,
                if (dragon.world.isRemote) "client" else "server"
            )
            currentPhase!!.initPhase()
        }
    }

    fun <T : IPhase> getPhase(phaseIn: PhaseType<T>): T
    {
        val i = phaseIn.id
        if (phases[i] == null)
        {
            phases[i] = phaseIn.createPhase(dragon)
        }
        return phases[i] as T
    }

    companion object
    {
        private val LOGGER = LogManager.getLogger()
    }

    init
    {
        setPhase(PhaseType.HOVER)
    }
}
