package phoenix.mixin

import com.google.common.collect.ImmutableList
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.screen.MainMenuScreen
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect

@Mixin(MainMenuScreen::class)
class MixinMainMenuScreen
{
    @Redirect(
        method = ["render"],
        at = At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/MainMenuScreen;drawCenteredString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"
        )
    )
    fun makeSplit(obj: MainMenuScreen, font: FontRenderer, string: String, x: Int, y: Int, color: Int)
    {
        val toDraw: List<String> = ImmutableList.copyOf(string.split("\n").toTypedArray())
        val scale = (toDraw.size * 1.2).toInt()
        RenderSystem.scalef(scale.toFloat(), scale.toFloat(), scale.toFloat())
        for (i in toDraw.indices)
        {
            obj.drawCenteredString(font, toDraw[i], x, y + (font.FONT_HEIGHT + 2) * (i - toDraw.size / 2), color)
        }
        RenderSystem.scalef(1.0f / scale, 1.0f / scale, 1.0f / scale)
    }
}
