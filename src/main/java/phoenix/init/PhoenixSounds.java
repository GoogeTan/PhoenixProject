package phoenix.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import phoenix.Phoenix;

import java.util.function.Supplier;

public class PhoenixSounds
{
    public static SoundEvent CHANGE_STAGE;

    public static void init()
    {
        CHANGE_STAGE = registerSound("change_stage");
    }

    private static SoundEvent registerSound(String key) {
        return Registry.register(Registry.SOUND_EVENT, key, new SoundEvent(new ResourceLocation(Phoenix.MOD_ID, key)));
    }
}