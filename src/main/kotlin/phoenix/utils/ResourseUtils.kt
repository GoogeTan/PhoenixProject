package phoenix.utils

import net.minecraft.util.ResourceLocation
import phoenix.Phoenix

object ResourseUtils
{
    @JvmStatic fun imgForDiary(name: String) = ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_img/$name")
    @JvmStatic fun key        (name: String) = ResourceLocation(Phoenix.MOD_ID, name)
    @JvmStatic fun block      (name: String) = ResourceLocation(Phoenix.MOD_ID, "textures/blocks/$name.png")
}