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
    int xSize, ySize;
    public ImageElement(ResourceLocation img, int xSize, int ySize)
    {
        this.img = img;
        Dimension d = TextureUtils.getTextureSize(img);
        this.w = d.width;
        this.h = d.height;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public int getHeight()
    {
        return (int) Math.ceil((double) w / xSize * h);
    }

    @Override
    public void render(ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int x, int y)
    {
        Dimension d = TextureUtils.getTextureSize(img);
        this.w = d.width;
        this.h = d.height;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getInstance().getTextureManager().bindTexture(img);
        RenderSystem.scaled(((double)w) / xSize, ((double)w) / xSize, ((double)w) / xSize);
        gui.blit((int) (x * ((double)xSize) / w), (int)(y * ((double)xSize) / w), 0, 0, w, h);
        RenderSystem.scaled(xSize / ((double) w), xSize / ((double) w), xSize / ((double) w));
    }
}
