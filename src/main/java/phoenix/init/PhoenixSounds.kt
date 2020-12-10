package phoenix.init

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import phoenix.Phoenix

@Mod.EventBusSubscriber(modid=Phoenix.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object PhoenixSounds
{
    var CHANGE_STAGE: SoundEvent = SoundEvent(ResourceLocation("change_stage"))

    @SubscribeEvent
    @JvmStatic
    fun resister(event: RegistryEvent.Register<SoundEvent>)
    {
        CHANGE_STAGE.registryName = ResourceLocation("change_stage")
        event.registry.register(CHANGE_STAGE)
    }
}