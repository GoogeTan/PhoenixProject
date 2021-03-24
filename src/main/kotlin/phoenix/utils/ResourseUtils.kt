package phoenix.utils

import net.minecraft.util.ResourceLocation
import phoenix.Phoenix

object ResourseUtils
{
    fun imgForDiary(name: String) = ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_img/$name")
    fun key        (name: String) = ResourceLocation(Phoenix.MOD_ID, name)
    fun block      (name: String) = ResourceLocation(Phoenix.MOD_ID, "textures/blocks/$name.png")
}