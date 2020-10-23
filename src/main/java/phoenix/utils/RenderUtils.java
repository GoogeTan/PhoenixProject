package phoenix.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class RenderUtils
{
    public static void drawWithScale(ResourceLocation texture, int sizeX, int sizeY, int x, int y, float scale)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        RenderSystem.scaled(scale, scale, scale);//увеличиваем картинку
        blit(x, y, 0, 0, sizeX, sizeY);
        RenderSystem.scaled(1 / scale, 1 / scale, 1 / scale);//возвращаем старый скейл, чтоб тект был нормальным
    }

    public static void drawTexturedRectWithSize(ResourceLocation texture, int textureSizeX, int textureSizeY, int x, int y, int sizeY, int sizeX)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        blit(x, y, 0, 0, textureSizeX, textureSizeY, sizeX, sizeY);
    }

    public static void blit(int x, int y, float offsetU, float offsetV, int textureSizeX, int textureSizeY)
    {
        blit(x, y, 0, offsetU, offsetV, textureSizeX, textureSizeY, 256, 256);
    }

    public static void blit(int x, int y, float offsetU, float offsetV, int textureSizeX, int textureSizeY, int renderSizeX, int renderSizeY)
    {
        blit(x, y, 0, offsetU, offsetV, textureSizeX, textureSizeY, renderSizeX, renderSizeY);
    }

    public static void blit(int x, int y, int blitOffset, float offsetU, float offsetV, int textureSizeX, int textureSizeY, int renderSizeY, int renderSizeX)
    {
        innerBlit(x, x + textureSizeX, y, y + textureSizeY, blitOffset, textureSizeX, textureSizeY, offsetU, offsetV, renderSizeX, renderSizeY);
    }

    private static void innerBlit(int startX, int endX, int startY, int endY, int blitOffset, int textureSizeX, int textureSizeY, float offsetU, float offsetV, int renderSizeX, int renderSizeY)
    {
        innerBlit(startX, endX, startY, endY, blitOffset,
                (offsetU) / (float) renderSizeX,
                (offsetU + (float) textureSizeX) / (float) renderSizeX,
                (offsetV) / (float) renderSizeY,
                (offsetV + (float) textureSizeY) / (float) renderSizeY);
    }

    protected static void innerBlit(int startX, int endX, int startY, int endY, int blitOffset, float startU, float endU, float startV, float endV)
    {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(startX, endY, blitOffset)  .tex(startU, endV).endVertex();
        bufferbuilder.pos(endX, endY, blitOffset)    .tex(endU, endV).endVertex();
        bufferbuilder.pos(endX, startY, blitOffset)  .tex(endU, startV).endVertex();
        bufferbuilder.pos(startX, startY, blitOffset).tex(startU, startV).endVertex();
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
    }
}
