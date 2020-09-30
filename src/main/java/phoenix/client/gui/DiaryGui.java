package phoenix.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ChangePageButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.tuple.Pair;
import phoenix.Phoenix;
import phoenix.client.gui.diaryPages.EDiaryChapter;
import phoenix.client.gui.diaryPages.FirstChapter;
import phoenix.client.gui.diaryPages.DiaryChapter;
import phoenix.client.gui.diaryPages.elements.ImageElement;
import phoenix.containers.DiaryContainer;
import phoenix.utils.DiaryUtils;
import phoenix.utils.ResourseUtils;
import phoenix.utils.StringUtils;
import phoenix.utils.TextureUtils;
import phoenix.utils.exeptions.BookException;

import java.util.ArrayList;

public class DiaryGui extends ContainerScreen<DiaryContainer>
{
    private static final ResourceLocation DIARY_TEXTURE = new ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_.png");
    private static final ResourceLocation DIARY_TEXTURE_2 = new ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_2.png");
    final DiaryChapter   diaryChapter;
    final DiaryContainer container;
    public DiaryGui(DiaryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        xSize *= 1.8;
        ySize *= 1.8;
        diaryChapter = new DiaryChapter(
                DiaryUtils.add(DiaryUtils.makeParagraph(Minecraft.getInstance().fontRenderer, xSize,
                    "Hear me, hear me, friend. \n " +
                    "I’m very, no, seriously ill. \n " +
                    "What’s the reason? \n" +
                    " This pain I do not understand. \n " +
                    "As if wind whistles, listen \n " +
                    "Over desolate, vacant, still field. \n " +
                    "Like a grove, leaves blazing I feel. \n " +
                    "And the drink sheds my leaves as I bend. \n "),
                Pair.of(3, new ImageElement(DIARY_TEXTURE, xSize - 30, ySize - 30))), xSize, font);

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
        /*
        ArrayList<String> currect_page = currect.getTextForPage(container.getPage());//массив со строками на странице
        for (int numder_of_string_on_page = 0; numder_of_string_on_page < currect_page.size(); ++numder_of_string_on_page)//рисуем каждую строку
        {
            //рендерер, срока, слева отступ 15 + размер страницы если 2 страница, сверху отступ 15 + кол-во строка на их высоту, цвет
            font.drawString(currect_page.get(numder_of_string_on_page), guiLeft + 15, guiTop + 15 + 15 * numder_of_string_on_page, TextFormatting.BLACK.getColor());
        }
        currect_page = currect.getTextForPage(container.getPage() + 1); //обновляем дял следующей страницы
        for (int numder_of_string_on_page = 0; numder_of_string_on_page < currect_page.size(); ++numder_of_string_on_page)//рисуем вторую страницу
        {
            font.drawString(currect_page.get(numder_of_string_on_page), guiLeft + 30 + this.xSize / 2, guiTop + 15 + 15 * numder_of_string_on_page, TextFormatting.BLACK.getColor());
        }
        */

    }
}