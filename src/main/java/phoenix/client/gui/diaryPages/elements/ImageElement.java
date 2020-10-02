package phoenix.client.gui.diaryPages.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.ResourceLocation;
import phoenix.Phoenix;
import phoenix.containers.DiaryContainer;
import phoenix.utils.TextureUtils;

import java.awt.*;

public class ImageElement implements IDiaryElement
{
    final ResourceLocation img;
    int w, h, xSize, ySize;
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
        Phoenix.LOGGER.error("logged" + (int) Math.ceil((double) w / xSize * h / 15D));
        return (int) Math.ceil((double) w / xSize * h / 15D);
    }

    @Override
    public void render(ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int x, int y)
    {
        RenderSystem.pushMatrix();
        double scale = ((double)w) / xSize;
        RenderSystem.scaled(scale, scale, scale);

        Minecraft.getInstance().getTextureManager().bindTexture(img);
        gui.blit((int) (x / scale), (int)(y / scale), 0, 0, w, h);

        RenderSystem.scaled(1 / scale, 1 / scale, 1 / scale);
        RenderSystem.popMatrix();
    }
}
