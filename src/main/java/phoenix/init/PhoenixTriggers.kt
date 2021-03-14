package phoenix.init

import net.minecraft.advancements.CriteriaTriggers
import phoenix.advancements.ChangeStageTrigger

object PhoenixTriggers
{
    lateinit var CHANGE_STAGE : ChangeStageTrigger

    fun register()
    {
        if(!this::CHANGE_STAGE.isInitialized)
            CHANGE_STAGE = CriteriaTriggers.register(ChangeStageTrigger)
    }
}