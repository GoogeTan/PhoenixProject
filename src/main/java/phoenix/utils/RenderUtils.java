package phoenix.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import javafx.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
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

    public static void drawRectScalable
            (ResourceLocation texture,
             int x, int y,
             double maxSizeX, double maxSizeY,
             int depth)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        Pair<Integer, Integer> d = TextureUtils.getTextureSize(texture);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        double scale = maxSizeX / d.getKey();
        int sizeX = (int) (d.getKey() * scale),
            sizeY = (int) (d.getValue() * scale);
        if(maxSizeY < sizeY)
        {
            scale *= maxSizeY / sizeY;
            sizeY = (int) (d.getKey() * scale);
            sizeX = (int) (d.getValue() * scale);
        }

        blit(x, y, depth, 0, 0, sizeX, sizeY, sizeX, sizeY);
    }

    public static void drawTexturedRectWithSize(ResourceLocation texture, int textureSizeX, int textureSizeY, int x, int y, int sizeX, int sizeY, int blitOffset)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        blit(x, y, blitOffset, 0, 0, sizeX, sizeY, textureSizeX, textureSizeY);
    }

    public static void drawTexturedRectWithSize(ResourceLocation texture, int textureSizeX, int textureSizeY, int x, int y, int sizeX, int sizeY, int blitOffset, int u, int v)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        AbstractGui.blit(x, y, blitOffset, (float) u, (float) v, sizeX, sizeY, textureSizeX, textureSizeY);
    }

    public static void blit(int x, int y, float offsetU, float offsetV, int sizeX, int sizeY)
    {
        blit(x, y, 0, offsetU, offsetV, sizeX, sizeY, 256, 256);
    }

    public static void blit(int x, int y, int depth, float offsetU, float offsetV, int sizeX, int sizeY, int texSizeX, int texSizeY)
    {
        draw(x, x + sizeX, y, y + sizeY, depth, offsetU, offsetV, texSizeX, texSizeY, sizeX, sizeY);
    }

    private static void draw(int posA, int posB, int posC, int posD, int depth, float offsetU, float offsetV, float texSizeX, float texSizeY, int sizeX, int sizeY)
    {
        //*
        innerBlit(posA, posB, posC, posD, depth,
                (offsetU + 0.00F) / texSizeX,
                (offsetU + sizeX) / texSizeX,
                (offsetV + 0.00F) / texSizeY,
                (offsetV + sizeY) / texSizeY);

        //*/
        /*
        innerBlit(posA, posB, posC, posD, depth,
                (offsetU + 0.00F) / texSizeX,
                (offsetU + texSizeX) / texSizeX,
                (offsetV + 0.00F) / texSizeY,
                (offsetV + texSizeY) / texSizeY);
        //*/
    }

    public static void innerBlit(int posA, int posB, int posC, int posD, int depth, float uvA, float uvB, float uvC, float uvD)
    {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(posA, posD, depth).tex(uvA, uvD).endVertex();
        bufferbuilder.pos(posB, posD, depth).tex(uvB, uvD).endVertex();
        bufferbuilder.pos(posB, posC, depth).tex(uvB, uvC).endVertex();
        bufferbuilder.pos(posA, posC, depth).tex(uvA, uvC).endVertex();
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
    }

    public static void refreshDrawing(IVertexBuilder vb, RenderType type) {
        if (vb instanceof BufferBuilder) {
            type.finish((BufferBuilder) vb, 0, 0, 0);
            ((BufferBuilder) vb).begin(type.getDrawMode(), type.getVertexFormat());
        }
    }
}
