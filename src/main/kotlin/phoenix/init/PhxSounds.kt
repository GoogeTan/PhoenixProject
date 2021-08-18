package phoenix.init

import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import phoenix.MOD_ID
import phoenix.Phoenix

@Mod.EventBusSubscriber(modid=MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
object PhxSounds
{
    var changeStage       : SoundEvent = phoenixSound("change_stage")
    val redoMusic         : SoundEvent = phoenixSound("redo_music")
    val getItemFromBarrel : SoundEvent = phoenixSound("put_smth_to_barrel")
    val getItemFromOven   : SoundEvent = phoenixSound("get_item_from_oven")

    @SubscribeEvent
    fun resister(event: RegistryEvent.Register<SoundEvent>)
    {
        event.registry.register(changeStage)
        event.registry.register(redoMusic)
        event.registry.register(getItemFromBarrel)
        event.registry.register(getItemFromOven)
    }

    private fun phoenixSound(nameIn: String): SoundEvent
    {
        val res = SoundEvent(ResourceLocation(MOD_ID, nameIn))
        res.registryName = ResourceLocation(MOD_ID, nameIn)
        return  res
    }
}