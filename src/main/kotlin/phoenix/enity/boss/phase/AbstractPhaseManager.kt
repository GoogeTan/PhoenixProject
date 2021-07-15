package phoenix.enity.boss.phase

import org.apache.logging.log4j.LogManager
import phoenix.enity.boss.AbstractEnderDragonEntity
import phoenix.world.StageManager

class AbstractPhaseManager(private val dragon: AbstractEnderDragonEntity)
{
    private val phases = arrayOfNulls<IPhase>(PhaseType.getTotalPhases())
    var currentPhase: IPhase? = null
        private set

    fun setPhase(phaseIn: PhaseType?)
    {
        if (currentPhase == null || phaseIn !== currentPhase?.type)
        {
            if (currentPhase != null)
                currentPhase!!.removeAreaEffect()

            currentPhase = getPhase(phaseIn)
            if (!dragon.world.isRemote)
                dragon.dataManager.set(AbstractEnderDragonEntity.PHASE, phaseIn!!.getId())
            LOGGER.debug("Dragon is now in phase {} on the {}", phaseIn, if (dragon.world.isRemote) "client" else "processServer")
            currentPhase?.initPhase()
        }
    }

    fun <T : IPhase> getPhase(phaseIn: PhaseType?): T?
    {
        val i = phaseIn?.getId()?:0

        if (phases[i] == null)
        {
            phases[i] = phaseIn?.createPhase(dragon)
        }

        return phases[i] as? T?
    }

    companion object
    {
        private val LOGGER = LogManager.getLogger()
    }

    init
    {
        setPhase(StageManager.stageEnum.hoverPhase)
    }
}
