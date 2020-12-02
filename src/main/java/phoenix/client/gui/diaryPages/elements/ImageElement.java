package phoenix.client.gui.diaryPages.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import phoenix.containers.DiaryContainer;
import phoenix.utils.RenderUtils;

public class ImageElement extends ADiaryElement
{
    int w, h;
    final ResourceLocation img;
    public ImageElement(ResourceLocation img, int textureX, int textureY)
    {
        this.img = img;
        w = textureX;
        h = textureY;
    }

    @Override
    public int getHeight(int maxSizeXIn, int maxSizeYIn)
    {
        double scale = scale(maxSizeXIn / 2, maxSizeYIn / 2);
        return (int) Math.ceil(h * scale);
    }

    public double scale(int maxSizeX, int maxSizeY)
    {
        if(w != 0 && h != 0)
        {
            double scale = maxSizeX / (double) w;
            double sizeX = (int) (w * scale),
                    sizeY = (int) (h * scale);
            if (maxSizeY < sizeY)
            {
                scale *= (double) maxSizeY / sizeY;
            }
            return scale;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public void render(ContainerScreen<DiaryContainer> gui, FontRenderer renderer, int xSize, int ySize, int x, int y, int depth)
    {
        double scale = scale(xSize / 2, ySize / 2);
        RenderSystem.pushMatrix();
        RenderSystem.scaled(scale, scale, scale);
        Minecraft.getInstance().getTextureManager().bindTexture(img);
        RenderUtils.drawRectScalable(img, x + 15, y + 15, xSize, ySize, depth);
        RenderSystem.scaled(1 / scale, 1 / scale, 1 / scale);
        RenderSystem.popMatrix();
    }
    //*
    @Override
    public String toString()
    {
        return img.toString();
    }
    //*/
    @Override
    public CompoundNBT serialize()
    {
        CompoundNBT res = new CompoundNBT();
        res.putString("type", "img");
        res.putString("res", img.getPath());
        //res.putInt("maxx", maxSizeX);
        //res.putInt("maxy", maxSizeY);
        return res;
    }
}
