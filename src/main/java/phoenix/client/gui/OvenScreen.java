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
    private static final ResourceLocation OVEN_TEXTURE = new ResourceLocation(Phoenix.MOD_ID, "textures/gui/oven.png");
    public OvenScreen(OvenContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        this.ySize = 114 + 172;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(OVEN_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(i, j, 0, 0, this.xSize, ySize - 34);
        super.render(p_render_1_, p_render_2_, p_render_3_);
    }
}
