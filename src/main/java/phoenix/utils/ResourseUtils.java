package phoenix.utils;

import net.minecraft.util.ResourceLocation;
import phoenix.Phoenix;

public class ResourseUtils
{
    public static ResourceLocation imgForDiary(String name)
    {
        return new ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_img/" + name);
    }

    public static ResourceLocation key(String name)
    {
        return new ResourceLocation(Phoenix.MOD_ID, name);
    }

    public static ResourceLocation block(String name)
    {
        return new ResourceLocation(Phoenix.MOD_ID, "textures/blocks/" + name + ".png");
    }
}
