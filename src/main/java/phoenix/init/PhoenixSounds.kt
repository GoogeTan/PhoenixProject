package phoenix.init

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.util.registry.Registry
import phoenix.Phoenix

object PhoenixSounds
{
    lateinit var CHANGE_STAGE: SoundEvent
    fun init()
    {
        CHANGE_STAGE = registerSound("change_stage")
    }

    private fun registerSound(key: String): SoundEvent
    {
        return Registry.register(Registry.SOUND_EVENT, key, SoundEvent(ResourceLocation(Phoenix.MOD_ID, key)))
    }
}