package phoenix.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.MainMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(MainMenuScreen.class)
public class MixinMainMenuScreen
{
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/MainMenuScreen;drawCenteredString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"))
    public void makeSplit(MainMenuScreen obj, FontRenderer font, String string, int x, int y, int color)
    {
        List<String> toDraw = ImmutableList.copyOf(string.split("\n"));
        int scale = (int) (toDraw.size() * 1.2);
        RenderSystem.scalef(scale, scale, scale);
        for (int i = 0; i < toDraw.size(); i++)
        {
            obj.drawCenteredString(font, toDraw.get(i), x, y + (font.FONT_HEIGHT + 2) * (i - toDraw.size() / 2), color);
        }
        RenderSystem.scalef(1.0f / scale, 1.0f /  scale, 1.0f /  scale);
    }
}
