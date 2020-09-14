package phoenix.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import javafx.util.Pair;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import phoenix.Phoenix;
import phoenix.client.gui.diaryPages.FirstChapter;
import phoenix.client.gui.diaryPages.DiaryChapter;
import phoenix.containers.DiaryContainer;
import phoenix.utils.DiaryUtils;
import phoenix.utils.StringUtils;

import java.util.ArrayList;

public class DiaryGui extends ContainerScreen<DiaryContainer>
{
    private static final ResourceLocation DIARY_TEXTURE = new ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_.png");
    final DiaryChapter currect;
    public DiaryGui(DiaryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        xSize *= 1.8;
        ySize *= 1.8;
        //currect = new FirstChapter();
        currect = DiaryUtils.makeChapterFromTranslate("phoenix.diary.ch1");
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
        int numder_of_string_on_page = 0;
        boolean page = false;
        for (int i = 0; i < 1 + (currect.countPages() > 1 ? 1 : 0); i++)
        {
            for (String currect : currect.getTextForPage(i))
            {
                //System.out.println(currect);
                ArrayList<String> words = StringUtils.stringToWords(currect);
                for (int number_of_words = 0; number_of_words < words.size(); number_of_words++)
                {
                    String string_to_print = "";

                    while (font.getStringWidth(string_to_print) < this.xSize / 2 - 30 && number_of_words < words.size())
                    {
                        string_to_print += words.get(number_of_words) + " ";
                        ++number_of_words;
                    }

                    //рендерер, срока, слева отступ 15 + размер страницы если 2 страница, сверху отступ 15 + кол-во строка на их высоту, цвет
                    font.drawString(string_to_print,
                            guiLeft + 15 + (page ? this.xSize / 2 + 15 : 0), guiTop + 15 + 15 * numder_of_string_on_page, TextFormatting.BLACK.getColor());

                    //странным майношрифтом
                    // this.drawString(font, string_to_print,
                    //      guiLeft + 15 + (page ? this.xSize / 2 + 15 : 0), guiTop + 15 + 15 * numder_of_string_on_page, TextFormatting.BLACK.getColor());

                    numder_of_string_on_page++;

                    if (numder_of_string_on_page >= 14 && !page)
                    {
                        page = true;
                        numder_of_string_on_page = 0;
                    }
                }
                numder_of_string_on_page++;
            }
        }
    }
}