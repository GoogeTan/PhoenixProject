package phoenix.init

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.util.registry.Registry
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import phoenix.Phoenix

@Mod.EventBusSubscriber(modid=Phoenix.MOD_ID)
object PhoenixSounds
{
    lateinit var CHANGE_STAGE: SoundEvent
    fun init()
    {
        CHANGE_STAGE = registerSound("change_stage")
    }

    private fun registerSound(key: String) = Registry.register(Registry.SOUND_EVENT, key, SoundEvent(ResourceLocation(Phoenix.MOD_ID, key)))

    @SubscribeEvent
    fun resister(event: RegistryEvent.Register<SoundEvent>)
    {
        event.registry.register(CHANGE_STAGE)
    }
}