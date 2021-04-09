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
    val REDO_MUSIC         : SoundEvent = phoenixSound("redo_music")
    val PUT_SMTH_TO_BARREL : SoundEvent = phoenixSound("put_smth_to_barrel")

    @SubscribeEvent
    fun resister(event: RegistryEvent.Register<SoundEvent>)
    {
        CHANGE_STAGE.registryName = ResourceLocation("change_stage")
        event.registry.register(CHANGE_STAGE)
        REDO_MUSIC.registryName = ResourceLocation("phoenix", "redo_music")
        event.registry.register(REDO_MUSIC)
        PUT_SMTH_TO_BARREL.registryName = ResourceLocation("phoenix", "put_smth_to_barrel")
        event.registry.register(PUT_SMTH_TO_BARREL)
    }

    private fun phoenixSound(nameIn: String) = SoundEvent(ResourceLocation(Phoenix.MOD_ID, nameIn))
}