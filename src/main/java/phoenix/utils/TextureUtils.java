package phoenix.utils;

import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class TextureUtils
{
    public static Pair<Integer, Integer> getTextureSize(@Nonnull ResourceLocation texture)
    {
        int prevTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, prevTexture);
        return new Pair<>(width, height);
    }
}