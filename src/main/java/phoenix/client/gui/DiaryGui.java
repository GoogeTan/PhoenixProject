package phoenix.client.gui;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import phoenix.containers.DiaryContainer;

public class DiaryGui extends ContainerScreen<DiaryContainer>
{
    public DiaryGui(DiaryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        font.drawString(title.getFormattedText(), (float)(guiLeft + xSize / 2 - font.getStringWidth(title.getFormattedText()) / 2), guiTop + 6.0F, 4210752);
        font.drawString(playerInventory.getDisplayName().getFormattedText(), guiLeft + 8.0F, guiTop + (float)(ySize - 96 + 2), 4210752);
    }
}
