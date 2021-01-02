package phoenix.init

import net.minecraft.advancements.CriteriaTriggers
import phoenix.advancements.ChangeStageTrigger

object PhoenixTriggers
{
    val CHANGE_STAGE = CriteriaTriggers.register(ChangeStageTrigger())

    fun register(){}
}