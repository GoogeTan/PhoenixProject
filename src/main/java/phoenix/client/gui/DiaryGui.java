package phoenix.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import javafx.util.Pair;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.ReadBookScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import phoenix.Phoenix;
import phoenix.client.gui.diaryPages.IDiaryPage;
import phoenix.containers.DiaryContainer;
import phoenix.utils.StringUtils;

import java.util.ArrayList;

public class DiaryGui extends ContainerScreen<DiaryContainer>
{
    private static final ResourceLocation DIARY_TEXTURE = new ResourceLocation(Phoenix.MOD_ID, "textures/gui/diary_.png");
    IDiaryPage currect;
    public DiaryGui(DiaryContainer screenContainer, PlayerInventory inv, ITextComponent titleIn)
    {
        super(screenContainer, inv, titleIn);
        xSize *= 2.5;
        ySize *= 2.5;
        currect = new IDiaryPage()
        {
            @Override
            public String[] getText()
            {
                return new String[]
                        {
                        "Hear me, hear me, friend. " +
                                "I’m very, no, seriously ill. " +
                                "What’s the reason?" +
                                " This pain I do not understand." +
                                "As if wind whistles, listen " +
                                "Over desolate, vacant, still field." +
                                "Like a grove, leaves blazing I feel." +
                                "And the drink sheds my leaves as I bend.",


                        "Head of mine is a-flapping my ears" +
                                "Like a migrating bird -- wings." +
                                "Near my neck, no legs want" +
                                "To waver and pause; to and fro." +
                                "Dark man, here he is," +
                                "Dark man, here he is," +
                                "On my bed, near me sits, haunts me." +
                                "Dark man…" +
                                "He won’t let me rest all night long."
                        };
            }

            @Override
            public ArrayList<Pair<Integer, ResourceLocation>> getImages()
            {
                return null;
            }
        };
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
    public void render(int p_render_1_, int p_render_2_, float p_render_3_)
    {
        int numder_of_string_on_page = 0;
        boolean page = false;
        for (String currect: currect.getText())
        {
            if(numder_of_string_on_page * 15 > ySize - 20)
            {
                page = true;
                numder_of_string_on_page = 0;
            }
            ArrayList<String> words = StringUtils.stringToWords(currect);
            String string_to_print = "";
            int number_of_words = 0;
            while(font.getStringWidth(string_to_print) < this.xSize / 2 - 20)
            {
                string_to_print += words.get(number_of_words) + " ";
                ++number_of_words;
            }

            //рендерер, срока, слева отступ 10 + размер страницы если 2 страница, сверху отступ 10 + кол-во строка на их высоту, цвет
            this.drawRightAlignedString(font, string_to_print,
                    guiLeft + 10 + (page ? xSize / 2 : 0), guiTop + 10 + 15 * numder_of_string_on_page, 255);
            numder_of_string_on_page++;
            System.out.println(string_to_print);
        }




        this.renderBackground();
        this.setFocused(null);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(DIARY_TEXTURE);
        RenderSystem.scaled(2, 2, 2);
        this.blit((int) (guiLeft * 0.6F), (int) (guiTop * 0.75F), 0, 0, 192, 192);
    }
}
