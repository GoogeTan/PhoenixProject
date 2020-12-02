package phoenix.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;

import java.util.function.Supplier;

public class PhoenixSounds
{
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Phoenix.MOD_ID);

    public static final RegistryObject<SoundEvent> UPDATER      = SOUNDS.register("updater", sound("oven_fire"));
    public static final RegistryObject<SoundEvent> CHANGE_STAGE = SOUNDS.register("change_stage", sound("change_stage"));


    public static void register()
    {
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static Supplier<SoundEvent> sound(String s)
    {
        return () -> new SoundEvent(new ResourceLocation(Phoenix.MOD_ID, s));
    }
}