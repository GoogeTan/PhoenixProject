package phoenix.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import phoenix.Phoenix;
import phoenix.containers.OvenContainer;
import phoenix.utils.RenderUtils;

public class OvenScreen extends ContainerScreen<OvenContainer>
{
    private static final ResourceLocation OVEN_TEXTURE = new ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_.png");
    public OvenScreen(OvenContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        this.setFocused(null);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(OVEN_TEXTURE);
        RenderSystem.scaled(2, 2, 2);//увеличиваем картинку
        RenderUtils.blit((int) (guiLeft * 0.5F), (int) (guiTop * 0.5F), 0, 0, 256, 256);
        RenderSystem.scaled(0.5, 0.5, 0.5);//возвращаем старый скейл, чтоб тект был нормальным

    }
}
