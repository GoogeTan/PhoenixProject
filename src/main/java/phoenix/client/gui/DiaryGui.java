package phoenix.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import phoenix.Phoenix;
import phoenix.client.gui.diaryPages.DiaryChapter;
import phoenix.client.gui.diaryPages.elements.IDiaryElement;
import phoenix.client.gui.diaryPages.elements.ImageElement;
import phoenix.containers.DiaryContainer;
import phoenix.utils.DiaryUtils;

import java.util.ArrayList;

public class DiaryGui extends ContainerScreen<DiaryContainer>
{
    private static final ResourceLocation DIARY_TEXTURE = new ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_.png");
    private static final ResourceLocation DIARY_TEXTURE_2 = new ResourceLocation(Phoenix.MOD_ID, "textures/gui/kawru.png");
     DiaryChapter   diaryChapter;
    final DiaryContainer container;
    public DiaryGui(DiaryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        xSize *= 1.8;
        ySize *= 1.8;
        ArrayList<IDiaryElement> par = new ArrayList<>();
        par.add(new ImageElement(DIARY_TEXTURE, xSize - 30, ySize - 30));
        par.addAll(DiaryUtils.makeParagraph(Minecraft.getInstance().fontRenderer, xSize,
                "Hear me, hear me, friend. \n " +
                "I’m very, no, seriously ill. \n " +
                "What’s the reason? \n" +
                "This pain I do not understand. \n " +
                "As if wind whistles, listen \n " +
                "Over desolate, vacant, still field. \n " +
                "Like a grove, leaves blazing I feel. \n " +
                "And the drink sheds my leaves as I bend. \n "));
        diaryChapter = new DiaryChapter(par, xSize, Minecraft.getInstance().fontRenderer);
        container = screenContainer;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {

    }


    @Override
    public void renderBackground()
    {
        this.setFocused(null);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(DIARY_TEXTURE);
        RenderSystem.scaled(2, 2, 2);//увеличиваем картинку
        this.blit((int) (guiLeft * 0.5F), (int) (guiTop * 0.5F), 0, 0, 256, 256);
        RenderSystem.scaled(0.5, 0.5, 0.5);//возвращаем старый скейл, чтоб тект был нормальным
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        this.renderBackground();
        diaryChapter.render(0, this, font, xSize, guiLeft, guiTop);
    }
}