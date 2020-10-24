package phoenix.client.gui.diaryPages.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.ResourceLocation;
import phoenix.Phoenix;
import phoenix.containers.DiaryContainer;
import phoenix.utils.RenderUtils;
import phoenix.utils.TextureUtils;

import java.awt.*;

public class ImageElement implements IDiaryElement
{
    final ResourceLocation img;
    int w, h, maxSizeX, maxSizeY;
    public ImageElement(ResourceLocation img, int maxSizeXIn, int maxSizeYIn)
    {
        this.img = img;
        Dimension d = TextureUtils.getTextureSize(img);
        this.w = d.width;
        this.h = d.height;
        this.maxSizeX = maxSizeXIn;
        this.maxSizeY = maxSizeYIn;
    }

    @Override
    public int getHeight()
    {
        Dimension d = TextureUtils.getTextureSize(img);
        if(d.width != 0 && d.height != 0)
        {
            double scale = maxSizeX / d.width;
            int sizeX = (int) (d.width * scale),
                    sizeY = (int) (d.height * scale);
            if (maxSizeY < sizeY)
            {
                scale = maxSizeY / sizeY;
            }
            double height = Math.ceil(scale * h / (Minecraft.getInstance().fontRenderer.FONT_HEIGHT + 4));
            Phoenix.LOGGER.error("logged " + height + " " + scale * h);
            return (int) height;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public void render(ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int ySize, int x, int y, int depth)
    {
        RenderSystem.pushMatrix();
        Minecraft.getInstance().getTextureManager().bindTexture(img);
        RenderUtils.drawRectScalable(img, x, y, xSize, ySize, depth);
        RenderSystem.popMatrix();
    }

    @Override
    public void serialise(CompoundNBT nbt)
    {
        nbt.putString("res", img.getPath());
    }
}
