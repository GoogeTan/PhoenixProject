package phoenix.init

import net.minecraft.advancements.CriteriaTriggers
import phoenix.advancements.ChangeStageTrigger
import phoenix.advancements.SitCaudaTrigger

object PhoenixTriggers
{
    lateinit var CHANGE_STAGE : ChangeStageTrigger
    lateinit var SIT_CAUDA    : SitCaudaTrigger

    fun register()
    {
        if(!this::CHANGE_STAGE.isInitialized)
            CHANGE_STAGE = CriteriaTriggers.register(ChangeStageTrigger)
        if(!this::SIT_CAUDA.isInitialized)
            SIT_CAUDA = CriteriaTriggers.register(SitCaudaTrigger)
    }
}