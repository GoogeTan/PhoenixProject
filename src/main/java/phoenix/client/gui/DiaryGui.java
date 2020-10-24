package phoenix.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import phoenix.Phoenix;
import phoenix.client.gui.diaryPages.DiaryBook;
import phoenix.client.gui.diaryPages.elements.IDiaryElement;
import phoenix.client.gui.diaryPages.elements.ImageElement;
import phoenix.containers.DiaryContainer;
import phoenix.utils.DiaryUtils;
import phoenix.utils.RenderUtils;

import java.util.ArrayList;

public class DiaryGui extends ContainerScreen<DiaryContainer>
{
    private static final ResourceLocation DIARY_TEXTURE = new ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_.png");
    private static final ResourceLocation DIARY_TEXTURE_2 = new ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_2.png");
    private DiaryBook diaryChapter = null;
    final DiaryContainer container;
    public DiaryGui(DiaryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        xSize *= 1.8;
        ySize *= 1.8;
        container = screenContainer;
    }

    public DiaryGui initGui()
    {
        diaryChapter = new DiaryBook(xSize, Minecraft.getInstance().fontRenderer);
        ArrayList<IDiaryElement> par = new ArrayList<>();
        par.addAll(DiaryUtils.makeParagraph(Minecraft.getInstance().fontRenderer, xSize - 15,
                "Hear me, hear me, friend." +
                        "I’m very, no, seriously ill." +
                        "What’s the reason?" +
                        "This pain I do not understand." +
                        "As if wind whistles, listen " +
                        "Over desolate, vacant, still field. " +
                        "Like a grove, leaves blazing I feel. " +
                        "And the drink sheds my leaves as I bend. "));
        par.add(new ImageElement(DIARY_TEXTURE, xSize - 30, ySize - 30));
        diaryChapter.add(par);
        return this;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
    }


    @Override
    public void renderBackground()
    {
        RenderUtils.drawRectScalable(DIARY_TEXTURE_2, guiLeft, guiTop, xSize * 1.0D, ySize * 1.0D, getBlitOffset());
    }


    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        if(diaryChapter == null) initGui();
        this.renderBackground();
        diaryChapter.render(0, this, font, xSize, ySize, guiLeft + 0,              guiTop, getBlitOffset());
        diaryChapter.render(1, this, font, xSize, ySize, guiLeft + xSize / 2 + 10, guiTop, getBlitOffset());
    }
}