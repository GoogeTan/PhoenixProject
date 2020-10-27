package phoenix.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import phoenix.Phoenix;
import phoenix.client.gui.diaryPages.DiaryBook;
import phoenix.client.gui.diaryPages.elements.ADiaryElement;
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
        container = screenContainer;
        xSize  *= 1.8;
        ySize *= 1.8;
   }

    @Override
    protected void init()
    {
        super.init();
        addButton(new InvisibleButton( guiLeft - 20,   guiTop, ySize, (button) -> diaryChapter.next(), true));
        addButton(new InvisibleButton( guiLeft + xSize - 10,guiTop, ySize, (button) -> diaryChapter.prev(), true));
    }

    public DiaryGui initGui()
    {
        diaryChapter = new DiaryBook(xSize - 30, ySize, Minecraft.getInstance().fontRenderer);
        ArrayList<ADiaryElement> par = new ArrayList<>();
        par.addAll(DiaryUtils.makeParagraph(Minecraft.getInstance().fontRenderer, xSize - 120,
                "Друг мой, друг мой, " +
                        "Я очень и очень болен. " +
                        "Сам не знаю, откуда взялась эта боль. " +
                        "То ли ветер свистит " +
                        "Над пустым и безлюдным полем, " +
                        "То ль, как рощу в сентябрь, " +
                        "Осыпает мозги алкоголь. " +
                        "Голова моя машет ушами, " +
                        "Как крыльями птица. " +
                        "Ей на шее ноги ",
                        "Маячить больше невмочь. " +
                        "Черный человек, " +
                        "Черный, черный, " +
                        "Черный человек " +
                        "На кровать ко мне садится, " +
                        "Черный человек " +
                        "Спать не дает мне всю ночь. " +
                        "Черный человек " +
                        "Водит пальцем по мерзкой книге " +
                        "И, гнусавя надо мной, " +
                        "Как над усопшим монах, ",
                        "Читает мне жизнь " +
                        "Какого-то прохвоста и забулдыги, " +
                        "Нагоняя на душу тоску и страх. " +
                        "Черный человек, " +
                        "Черный, черный! " +
                        "«Слушай, слушай, — " +
                        "Бормочет он мне, — " +
                        "В книге много прекраснейших " +
                        "Мыслей и планов. ",
                        "Этот человек " +
                        "Проживал в стране " +
                        "Самых отвратительных " +
                        "Громил и шарлатанов. " +
                        "В декабре в той стране " +
                        "Снег до дьявола чист, " +
                        "И метели заводят " +
                        "Веселые прялки. " +
                        "Был человек тот авантюрист, " +
                        "Но самой высокой " +
                        "И лучшей марки. " ,
                        "Был он изящен, " +
                        "К тому ж поэт, " +
                        "Хоть с небольшой, " +
                        "Но ухватистой силою, " +
                        "И какую-то женщину, " +
                        "Сорока с лишним лет, " +
                        "Называл скверной девочкой " +
                        "И своею милою. " ,
                        "Счастье, — говорил он, — " +
                        "Есть ловкость ума и рук. " +
                        "Все неловкие души " +
                        "За несчастных всегда известны. " +
                        "Это ничего, " +
                        "Что много мук " +
                        "Приносят изломанные " +
                        "И лживые жесты. " +
                        "В житейскую стынь, " +
                        "При тяжелых утратах " ,
                        "И когда тебе грустно, " +
                        "Казаться улыбчивым и простым — " +
                        "Самое высшее в мире искусство». " +
                        "«Черный человек! " +
                        "Ты не смеешь этого! " +
                        "Ты ведь не на службе " +
                        "Живешь водолазовой. " +
                        "Что мне до жизни " +
                        "Скандального поэта. " ,
                        "Пожалуйста, другим " +
                        "Читай и рассказывай». " +
                        "Черный человек " +
                        "Глядит на меня в упор. " +
                        "И глаза покрываются " +
                        "Голубой блевотой, — " +
                        "Словно хочет сказать мне, " +
                        "Что я жулик и вор, " +
                        "Так бесстыдно и нагло " +
                        "Обокравший кого-то. " +
                        "........................ " +
                        "Друг мой, друг мой, " +
                        "Я очень и очень болен. " ,
                        "Сам не знаю, откуда взялась эта боль. " +
                        "То ли ветер свистит " +
                        "Над пустым и безлюдным полем, " +
                        "То ль, как рощу в сентябрь, " +
                        "Осыпает мозги алкоголь. " +
                        "Ночь морозная. " +
                        "Тих покой перекрестка. " +
                        "Я один у окошка, " +
                        "Ни гостя, ни друга не жду. " +
                        "Вся равнина покрыта " +
                        "Сыпучей и мягкой известкой, " +
                        "И деревья, как всадники, " +
                        "Съехались в нашем саду. ",
                        "Где-то плачет " +
                        "Ночная зловещая птица. " +
                        "Деревянные всадники " +
                        "Сеют копытливый стук. " +
                        "Вот опять этот черный " +
                        "На кресло мое садится, " +
                        "Приподняв свой цилиндр " +
                        "И откинув небрежно сюртук. "));
        //par.add(new ImageElement(DIARY_TEXTURE, xSize - 30 - 40, ySize - 30));
        for (ADiaryElement el : par)
            System.out.println(el);
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
    public void render(int p1, int p2, float p3)
    {
        super.render(p1, p2, p3);
        if(diaryChapter == null) initGui();

        this.renderBackground();

        diaryChapter.render(this, font, xSize, ySize, guiLeft, guiTop, getBlitOffset());

        for (Widget button : this.buttons)
        {
            button.render(p1, p2, p3);
        }
    }
}