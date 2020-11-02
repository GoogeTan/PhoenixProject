package phoenix.init;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class PhoenixKeyBindings
{
    private static final String category = "Phoenix Project";
    public static final KeyBinding
            MY_KEY_FIRST = new KeyBinding("phoenix.keys.first", 70, category),
            MY_KEY_SECOND = new KeyBinding("phoenix.keys.second", 71 , category);

    public static void register()
    {
        setRegister(MY_KEY_FIRST);
        setRegister(MY_KEY_SECOND);
    }

    private static void setRegister(KeyBinding binding)
    {
        ClientRegistry.registerKeyBinding(binding);
    }
}
