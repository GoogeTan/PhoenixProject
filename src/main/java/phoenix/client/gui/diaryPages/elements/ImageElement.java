package phoenix.client.gui.diaryPages.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.ResourceLocation;
import phoenix.containers.DiaryContainer;
import phoenix.utils.TextureUtils;

import java.awt.*;

public class ImageElement implements IDiaryElement
{
    final ResourceLocation img;
    int w, h;
    int scale = 1, xSize, ySize;
    public ImageElement(ResourceLocation img, int xSize, int ySize)
    {
        this.img = img;
        Dimension d = TextureUtils.getTextureSize(img);
        this.w = d.width;
        this.h = d.height;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public void setScale(int scale)
    {
        this.scale = scale;
    }

    @Override
    public int getHeight()
    {
        return (int) Math.ceil((double) xSize / (double)  w *  (double) h / 15D);
    }

    @Override
    public void render(ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int x, int y)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(img);
        RenderSystem.scaled(scale, scale, scale);//увеличиваем картинку
        gui.blit(x / scale, y / scale, 0, 0, xSize, xSize / w * h);
        RenderSystem.scaled(1D / scale, 1D / scale, 1D / scale);//возвращаем старый скейл, чтоб тект был нормальным
    }
}
